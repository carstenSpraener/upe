package upe.resource.model;

import upe.process.messages.UProcessMessage;

import java.util.ArrayList;
import java.util.List;

public class ProcessElementDelta {
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

    public ProcessElementDelta from(ProcessElementState state) {
        this.elementPath = state.fieldPath;
        this.valueForFrontend = state.valueForFrontend;
        this.isVisible = state.visible;
        this.enabled = state.enabled;
        this.newMessages = state.newMessages;
        this.removedMessages = null;
        this.severity = 0;
        for( var msg : this.newMessages ) {
            if( msg.getMessageLevel() > this.severity ) {
                this.severity = msg.getMessageLevel();
            }
        }
        return this;
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
}
