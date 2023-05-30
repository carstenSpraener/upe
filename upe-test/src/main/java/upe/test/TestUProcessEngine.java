package upe.test;

import upe.process.engine.BaseUProcessEngine;
import upe.process.messages.UProcessMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * This is an implementation of an UProcessEngine to use in tests. It is not
 * bound to any backend or runtime environment except plain java.
 *
 */
public class TestUProcessEngine extends BaseUProcessEngine {
    private List<UProcessMessage> messageList = new ArrayList<>();

    @Override
    public void queueProcessMessage(UProcessMessage msg) {
        super.queueProcessMessage(msg);
        this.messageList.add(msg);
    }

    public List<UProcessMessage> getQueuedMessages() {
        return messageList;
    }

    public boolean containsQueuedMessage(String msgID) {
        for(UProcessMessage msg : this.messageList ) {
            if( msg.getMessageID().equals(msgID)) {
                return true;
            }
        }
        return false;
    }
}
