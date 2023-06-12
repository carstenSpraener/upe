package upe.resource.model;

import upe.process.UProcessElement;
import upe.process.messages.UProcessMessage;

import java.util.ArrayList;
import java.util.List;

public class ProcessElementDelta {
    private static long peDeltaCount = 0;

    Long peDeltaID = peDeltaCount++;
    String elementPath;
    Boolean isVisible;
    Boolean enabled;
    String valueForFrontend;
    List<UProcessMessage> newMessages = new ArrayList<>();;
    List<UProcessMessage> removedMessages = new ArrayList<>();
    Integer severity;

    boolean hasDelta() {
        return isVisible!=null || enabled != null || valueForFrontend != null || newMessages.size()>0 || removedMessages.size()>0;
    }

    public Long getPeDeltaID() {
        return peDeltaID;
    }

    public String getElementPath() {
        return elementPath;
    }

    public Boolean getVisible() {
        return isVisible;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public String getValueForFrontend() {
        return valueForFrontend;
    }

    public List<UProcessMessage> getNewMessages() {
        return newMessages;
    }

    public List<UProcessMessage> getRemovedMessages() {
        return removedMessages;
    }

    public Integer getSeverity() {
        return severity;
    }

    @Override
    public String toString() {
        return "ProcessElementDelta{" +
                "id=" + peDeltaID + '\'' +
                ", elementPath='" + elementPath + '\'' +
                ", isVisible=" + isVisible +
                ", enabled=" + enabled +
                ", valueForFrontend='" + valueForFrontend + '\'' +
                ", newMessages=" + newMessages +
                ", removedMessages=" + removedMessages +
                ", severity=" + severity +
                '}';
    }

    public ProcessElementDelta from(ProcessElementState elementState) {
        this.isVisible = elementState.visible;
        this.enabled = elementState.enabled;
        this.newMessages = elementState.newMessages;
        this.removedMessages = elementState.removedMessages;
        this.valueForFrontend = elementState.valueForFrontend;
        this.elementPath = elementState.fieldPath;
        this.severity = elementState.severity;
        return this;
    }
}
