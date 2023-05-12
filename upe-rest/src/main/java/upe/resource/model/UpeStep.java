package upe.resource.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UpeStep {
    private int stepNr;
    private String type;
    private String fieldPath;
    private String oldValue;
    private String newValue;

    public static final String FIELD_LIST= "DIALOG_ID, STEP_NR, TYPE, FIELD, OLD_VALUE, NEW_VALUE";

    public UpeStep(int stepNr, String fieldPath, String oldValue, String newValue) {
        this.type="VC";
        this.stepNr = stepNr;
        this.fieldPath = fieldPath;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public UpeStep(ResultSet rs) throws SQLException {
        this(
                rs.getInt(2), // StepNr
                rs.getString(4), // FieldPath
                rs.getString(6), // oldValue
                rs.getString(5) // newValue
        );
        this.type = rs.getString(3); //Type
    }

    public UpeStep(int stepNr, String fieldPath) {
        this(stepNr,fieldPath,null,null);
        this.type = "AC";
    }

    public static String typeOf(String changedFieldPath, String oldValue, String newValue) {
        if( changedFieldPath.startsWith("@") ) {
            return "LC";
        }
        if( oldValue==null && newValue==null) {
            return "AC";
        } else {
            return "VC";
        }
    }

    public int getStepNr() {
        return stepNr;
    }

    public String getFieldPath() {
        return fieldPath;
    }

    public String getOldValue() {
        return oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    @Override
    public String toString() {
        return "UpeStep{" +
                "stepNr=" + stepNr +
                ", fieldPath='" + fieldPath + '\'' +
                ", oldValue='" + oldValue + '\'' +
                ", newValue='" + newValue + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }
}
