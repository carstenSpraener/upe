package upe.resource.model;

import com.google.gson.*;
import upe.process.messages.UProcessMessage;
import upe.process.messages.UProcessMessageImpl;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
class UpeMessageClassTypeAdapter implements JsonDeserializer<UProcessMessage> {

    @Override
    public UProcessMessage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return context.deserialize(json, UProcessMessageImpl.class);
    }
}
public class UpeStep {
    private int stepNr;
    private String type;
    private String fieldPath;
    private String oldValue;
    private String newValue;
    private ProcessDelta delta;

    public static final String FIELD_LIST= "DIALOG_ID, STEP_NR, TYPE, FIELD, OLD_VALUE, NEW_VALUE, DELTA_JSON";

    public UpeStep(int stepNr, String fieldPath, String oldValue, String newValue, String delta) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(UProcessMessage.class, new UpeMessageClassTypeAdapter())
                .create();
        this.type="VC";
        this.stepNr = stepNr;
        this.fieldPath = fieldPath;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.delta = gson.fromJson(delta, ProcessDelta.class);
    }

    public UpeStep(ResultSet rs) throws SQLException {
        this(
                rs.getInt(2),    // StepNr
                rs.getString(4), // FieldPath
                rs.getString(6), // oldValue
                rs.getString(5), // newValue
                rs.getString(7)  // ProcessDelta post applied
        );
        this.type = rs.getString(3); //Type
    }

    public UpeStep(int stepNr, String fieldPath) {
        this(stepNr,fieldPath,null,null, null);
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

    public ProcessDelta getDelta() {
        return this.delta;
    }
}
