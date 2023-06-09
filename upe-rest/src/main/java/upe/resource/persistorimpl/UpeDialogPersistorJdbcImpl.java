package upe.resource.persistorimpl;

import com.google.gson.Gson;
import upe.exception.UPERuntimeException;
import upe.resource.UpeDialogPersistor;
import upe.resource.model.ProcessDelta;
import upe.resource.model.UpeDialogState;
import upe.resource.model.UpeStep;

import java.sql.*;

public class UpeDialogPersistorJdbcImpl implements UpeDialogPersistor {
    private static UpeDialogPersistorJdbcImpl instance = null;
    private static boolean INITIALIZED = Boolean.FALSE;
    private static int MAX_DELTA_SIZE = 8192*2;

    private String driverClass;
    private String jdbcURL;
    private String user;
    private String password;
    private Gson deltaGson;
    private Connection connection;

    private UpeDialogPersistorJdbcImpl() {
        this.driverClass = System.getProperty("upe.persistor.jdbc.driver", "org.hsqldb.jdbc.JDBCDriver");
        this.jdbcURL = System.getProperty("upe.persistor.jdbc.url", "jdbc:hsqldb:mem:upe-dialog");
        //this.user = System.getProperty("upe.persistor.jdbc.user", "sa");
        //this.password = System.getProperty("upe.persistor.jdbc.password", "sa");
    }

    public static final UpeDialogPersistorJdbcImpl intance(Gson deltaGson) {
        if( instance==null ) {
            instance = new UpeDialogPersistorJdbcImpl();
            instance.deltaGson = deltaGson;
            try {
                Class.forName(instance.driverClass);
                initiateDataModel(instance.getConnection());
            } catch( ClassNotFoundException cnfExc ) {
                throw new RuntimeException(cnfExc);
            } catch( SQLException sqlXC ) {
                throw new RuntimeException(sqlXC);
            }
        }
        return instance;
    }

    private static void initiateDataModel(Connection con) {
        if( !INITIALIZED ) {
            synchronized (UpeDialogPersistorJdbcImpl.class) {
                if( !INITIALIZED ) {
                    try {
                        con.createStatement().execute("create table UPE_DIALOGID_SEQ (DIALOG_ID INTEGER);");
                        con.createStatement().execute("create table UPE_DIALOG_STATE (" +
                                "  DIALOG_ID VARCHAR(128) NOT NULL," +
                                "  STEP_COUNT INTEGER NOT NULL," +
                                "  PRIMARY KEY (DIALOG_ID)" +
                                ");");
                        con.createStatement().execute("create table UPE_DIALOG_STEP (" +
                                "  DIALOG_ID VARCHAR(128) NOT NULL," +
                                "  STEP_NR INTEGER NOT NULL," +
                                "  TYPE CHAR(2) NOT NULL," +
                                "  FIELD VARCHAR(1024)," +
                                "  NEW_VALUE VARCHAR(1024),"+
                                "  OLD_VALUE VARCHAR(1024)," +
                                "  DELTA_JSON VARCHAR("+MAX_DELTA_SIZE+")," +
                                "  PRIMARY KEY (DIALOG_ID,STEP_NR)" +
                                ");");
                        INITIALIZED = true;
                    } catch( SQLSyntaxErrorException ssXC ) {
                        // Dannn gibt es das Datenmodell schon.
                        if (ssXC.getSQLState().equals("42504")) {
                            System.out.println(": " + ssXC.getMessage());
                        } else {
                            throw new RuntimeException(ssXC);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("Exception while initializing database", e);
                    }
                }
            }
        }
    }

    public Connection getConnection() throws SQLException {
        if( this.connection == null || this.connection.isClosed() ) {
            if( this.user != null ) {
                this.connection = DriverManager.getConnection(this.jdbcURL, this.user, this.password);
            } else {
                this.connection = DriverManager.getConnection(this.jdbcURL);
            }
        }
        return connection;
    }


    @Override
    public UpeDialogState storeAction(String dialogID, int stepCount, String changedFieldPath, String deltaSjon) {
        return storeStep(dialogID,stepCount,changedFieldPath,null,null, deltaSjon);
    }

    @Override
    public UpeDialogState storeStep(String dialogID, int stepCount, String changedFieldPath, String oldValue, String newValue, String deltaJson) {
        try {
            Connection con = getConnection();
            con.setAutoCommit(false);
            int upeDialogStep = readStepCount(con, dialogID);
            if( stepCount > 0 && upeDialogStep+1 != stepCount ) {
                throw new IllegalStateException("Step sequence broken. DB-Step is "+upeDialogStep+", Update-Step is "+stepCount);
            }
            String type = UpeStep.typeOf(changedFieldPath,oldValue,newValue);
            String insertQL = "insert into UPE_DIALOG_STEP (" + UpeStep.FIELD_LIST+") "+
                    "values (" +
                    "'"+dialogID+"', "+stepCount+" , '"+type+"', '"+changedFieldPath+"', '"+newValue+"', '"+oldValue+"', '"+deltaJson+"')";
            getConnection().createStatement().execute(insertQL);
            getConnection().createStatement().execute("update UPE_DIALOG_STATE set STEP_COUNT="+stepCount+" where DIALOG_ID='"+dialogID+"' and STEP_COUNT="+(stepCount-1));
            getConnection().commit();
            return restore(dialogID, deltaGson);
        } catch( SQLException sqlXC ) {
            throw new RuntimeException("Exception while storing step: "+sqlXC.getMessage(), sqlXC);
        }
    }

    private int readStepCount(Connection con, String dialogID) throws SQLException {
        ResultSet rs = con.createStatement().executeQuery("select STEP_COUNT from UPE_DIALOG_STATE where DIALOG_ID='"+dialogID+"'");
        if( rs.next() ) {
            return rs.getInt(1);
        }
        throw new IllegalArgumentException("No such dialog found: "+dialogID);
    }

    @Override
    public UpeDialogState restore(String dialogID, Gson deltaGson) {
        try {
            Connection con = getConnection();
            con.setAutoCommit(false);
            ResultSet rs = con.createStatement().executeQuery("select DIALOG_ID, STEP_COUNT from UPE_DIALOG_STATE where DIALOG_ID='"+dialogID+"'");
            if( !rs.next() ) {
                throw new IllegalArgumentException("No such dialog found: "+dialogID);
            }
            UpeDialogState dialogState = new UpeDialogState(rs.getString(1), rs.getInt(2));
            ResultSet rsSteps = con.createStatement().executeQuery("select "+UpeStep.FIELD_LIST+" from UPE_DIALOG_STEP " +
                    "where DIALOG_ID='"+dialogID+"' order by STEP_NR");
            while( rsSteps.next( ) ) {
                dialogState.getSteps().add(createUpeStep(rsSteps, deltaGson));
            }
            con.commit();
            return dialogState;
        } catch( SQLException sXC ) {
            throw new RuntimeException(sXC);
        }
    }

    private UpeStep createUpeStep(ResultSet rs, Gson deltaGson) throws SQLException {
        UpeStep step = new UpeStep(rs, deltaGson);
        return step;
    }

    @Override
    public UpeDialogState initiate() {
        try {
            Connection con = getConnection();
            con.setAutoCommit(false);
            int id = readNextDialogID(con);
            UpeDialogState result = new UpeDialogState("DIALOGSTATE_"+id);
            con.createStatement().execute("insert into UPE_DIALOG_STATE (DIALOG_ID,STEP_COUNT) values ('"+result.getDialogID()+"',0)");
            con.commit();
            return restore(result.getDialogID(), deltaGson);
        }  catch( SQLException sXC ) {
        throw new RuntimeException(sXC);
        }
    }

    private int readNextDialogID(Connection con) throws SQLException {
        ResultSet rs = con.createStatement().executeQuery("SELECT DIALOG_ID from UPE_DIALOGID_SEQ;");
        if( rs.next() ) {
            int dialogID = rs.getInt(1);
            int nextDialogID = dialogID+1;
            con.createStatement().execute("update UPE_DIALOGID_SEQ set DIALOG_ID="+nextDialogID+" where DIALOG_ID="+dialogID);
            con.commit();
            return nextDialogID;
        } else {
            con.createStatement().execute("insert into UPE_DIALOGID_SEQ (DIALOG_ID) values 1;");
            con.commit();
            return 1;
        }
    }
}
