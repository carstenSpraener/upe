package upe.resource.model;

import upe.process.UProcessField;
import upe.process.messages.UProcessMessage;

import java.util.ArrayList;
import java.util.List;

public class ProcessElementState {
    public String fieldPath;
    public Boolean visible;
    public Boolean enabled;
    public String valueForFrontend;
    public int severity;
    public List<UProcessMessage> originalMessages = new ArrayList<>();
    public List<UProcessMessage> newMessages = new ArrayList<>();
    public List<UProcessMessage> removedMessages = new ArrayList<>();

    public ProcessElementState(UProcessField pField) {
        this.fieldPath = pField.getElementPath();
        this.visible = pField.isVisible();
        this.enabled = pField.isEnabled();
        this.valueForFrontend = pField.getValueForFrontend();
        this.originalMessages = new ArrayList<>();
        for (UProcessMessage msg : pField.getMessages()) {
            this.originalMessages.add(msg);
        }
    }

    private ProcessElementState(String fieldPath) {
        this.fieldPath = fieldPath;
    }

    public static ProcessElementState fromNewField(String elementPath) {
        return new ProcessElementState(elementPath);
    }

    public ProcessElementDelta getDelta(UProcessField pField) {
        if (!pField.getElementPath().equals(this.fieldPath)) {
            throw new IllegalArgumentException("Recorded field " + this.fieldPath + " and compared field " + pField.getElementPath() + " do not match");
        }
        boolean hasChanged = false;
        if (this.enabled == null || pField.isEnabled() != this.enabled) {
            this.enabled = pField.isEnabled();
            hasChanged = true;
        }
        if (this.visible == null || pField.isVisible() != visible) {
            this.visible = pField.isVisible();
            hasChanged = true;
        }
        if (hasValueChanged(pField.getValueForFrontend(), this.valueForFrontend)) {
            this.valueForFrontend = pField.getValueForFrontend();
            hasChanged = true;
        }
        for (UProcessMessage msg : pField.getMessages()) {
            // the message is not in the original message list.
            if (!this.originalMessages.contains(msg)) {
                this.newMessages.add(msg);
                hasChanged = true;
            }
            // Remove the message from the message list. It is either new
            // or still remaining.
            this.originalMessages.remove(msg);
        }
        // Now only removed messages are left in the original message list.
        if( this.originalMessages.size() > 0 ) {
            this.removedMessages.addAll(this.originalMessages);
            hasChanged = true;
        }
        this.severity = 0;
        for (var msg : pField.getMessages()) {
            if (msg.getMessageLevel() > severity) {
                severity = msg.getMessageLevel();
            }
        }
        if (hasChanged) {
            ProcessElementDelta delta = new ProcessElementDelta();
            delta.from(this);
            return delta;
        } else {
            return null;
        }
    }

    private boolean hasValueChanged(String actualValue, String oldValue) {
        if (actualValue == null && oldValue != null) {
            return true;
        }
        if (actualValue != null && oldValue == null) {
            return true;
        }
        if (actualValue == null && oldValue == null) {
            return false;
        }
        return !actualValue.equals(oldValue);
    }

    public String getFieldPath() {
        return fieldPath;
    }

    public ProcessElementDelta buildCompleteDelta() {
        ProcessElementDelta delta = new ProcessElementDelta();
        this.newMessages = this.originalMessages;
        return delta.from(this);
    }
}
