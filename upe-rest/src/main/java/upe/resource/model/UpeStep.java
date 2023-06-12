package upe.resource.model;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import upe.process.messages.UProcessMessage;
import upe.process.messages.UProcessMessageImpl;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UpeStep {
    private int stepNr;
    private List<ProcessDelta> deltaList;

    public static final String FIELD_LIST= "DIALOG_ID, STEP_NR, PAYLOAD_JSON";

    public UpeStep(int stepNr, String payLoad, Gson gson) {
        this.stepNr = stepNr;
        Type listType = new TypeToken<ArrayList<ProcessDelta>>(){}.getType();
        this.deltaList = gson.fromJson(payLoad, listType);
    }

    public UpeStep(ResultSet rs, Gson gson) throws SQLException {
        this(
                rs.getInt(2),    // StepNr
                rs.getString(3),  // ProcessDelta post applied
                gson
        );
    }

    public int getStepNr() {
        return stepNr;
    }

    public List<ProcessDelta> getDeltaList() {
        return this.deltaList;
    }
}
