package upe.resource.model;

import com.google.gson.annotations.JsonAdapter;

import java.util.ArrayList;
import java.util.List;

public class UpeDialogState {
    private RsrcLink previous;
    private RsrcLink next;
    private RsrcLink update;
    private String dialogID;
    private int stepCount = 0;
    private String processName;
    private String activeView;

    private transient List<UpeStep> steps = new ArrayList<>();

    public UpeDialogState(String dialogID) {
        this.dialogID = dialogID;
    }

    public UpeDialogState(String dialogID, int stepCount) {
        this(dialogID);
        this.stepCount = stepCount;
    }

    public RsrcLink getPrevious() {
        return previous;
    }

    public RsrcLink getNext() {
        return next;
    }

    public RsrcLink getUpdate() {
        return update;
    }

    public String getDialogID() {
        return dialogID;
    }

    public int getStepCount() {
        return stepCount;
    }

    public List<UpeStep> getSteps() {
        return steps;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getActiveView() {
        return activeView;
    }

    public void setActiveView(String activeView) {
        this.activeView = activeView;
    }

    @Override
    public String toString() {
        return "UpeDialogState{" +
                "previous=" + previous +
                ", next=" + next +
                ", update=" + update +
                ", dialogID='" + dialogID + '\'' +
                ", stepCount=" + stepCount +
                ", steps=" + steps +
                '}';
    }

    public void setStepCount(int stepNr) {
        this.stepCount = stepNr;
    }
}
