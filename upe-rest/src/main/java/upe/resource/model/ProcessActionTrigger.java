package upe.resource.model;

public class ProcessActionTrigger {
    private String dialogID;
    private String step;
    private String fieldPath;

    public ProcessActionTrigger() {
    }

    public String getDialogID() {
        return dialogID;
    }

    public void setDialogID(String dialogID) {
        this.dialogID = dialogID;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getFieldPath() {
        return fieldPath;
    }

    public void setFieldPath(String fieldPath) {
        this.fieldPath = fieldPath;
    }

}
