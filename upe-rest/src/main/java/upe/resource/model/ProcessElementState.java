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
    public List<UProcessMessage> newMessages;

    public ProcessElementState(UProcessField pField) {
        this.fieldPath = pField.getElementPath();
        this.visible = pField.isVisible();
        this.enabled = pField.isEnabled();
        this.valueForFrontend = pField.getValueForFrontend();
        this.newMessages = new ArrayList<>();
        for (UProcessMessage msg : pField.getMessages()) {
            this.newMessages.add(msg);
        }
    }

    private ProcessElementState(String fieldPath) {
        this.fieldPath = fieldPath;
        this.newMessages = new ArrayList<>();
    }

    public static ProcessElementState fromNewField(String elementPath) {
        return new ProcessElementState(elementPath);
    }

    public ProcessElementDelta getDelta(UProcessField pField) {
        if (!pField.getElementPath().equals(this.fieldPath)) {
            throw new IllegalArgumentException("Recorded field " + this.fieldPath + " and compared field " + pField.getElementPath() + " do not match");
        }
        ProcessElementDelta delta = new ProcessElementDelta();
        if (this.enabled == null || pField.isEnabled() != this.enabled) {
            delta.enabled = pField.isEnabled();
        }
        if (this.visible == null || pField.isVisible() != visible) {
            delta.isVisible = pField.isVisible();
        }
        if (hasValueChanged(pField.getValueForFrontend(), this.valueForFrontend)) {
            delta.valueForFrontend = pField.getValueForFrontend();
        }
        for (UProcessMessage msg : pField.getMessages()) {
            if (!this.newMessages.contains(msg)) {
                delta.newMessages.add(msg);
            }
        }
        for (UProcessMessage msg : this.newMessages) {
            if (!pField.getMessages().contains(msg)) {
                delta.removedMessages.add(msg);
            }
        }
        int severity = 0;
        for (var msg : pField.getMessages()) {
            if (msg.getMessageLevel() > severity) {
                severity = msg.getMessageLevel();
            }
        }
        delta.severity = severity;
        if (delta.hasDelta()) {
            delta.elementPath = pField.getElementPath();
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
}
