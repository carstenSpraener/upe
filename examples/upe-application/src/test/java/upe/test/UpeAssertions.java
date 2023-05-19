package upe.test;

import upe.process.UProcessComponent;
import upe.process.engine.BaseUProcessEngine;
import upe.process.messages.UProcessMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpeAssertions {
    private UProcessComponent pc;

    public UpeAssertions(UProcessComponent pc) {
        this.pc = pc;
    }

    public void assertHasError(String elementPath, String msgID) {
        String containedMessageID = pc.getProcessElement(elementPath).getMessages().stream()
                .filter(msg -> msg.getMessageID().equals(msgID))
                .map(msg->msg.getMessageID())
                .findFirst().orElse(null);
        if( containedMessageID==null ) {
            throw new UPEAssertionException("ProcessField '"+elementPath+"' does not contain message with id '"+msgID+"'.");
        }
    }

    public void assertMaxMsgLevel(String elementPath, int messageLevelError) {
        if( messageLevelError != this.pc.getProcessElement(elementPath).getMaximumMessageLevel() ) {
            int level = this.pc.getProcessElement(elementPath).getMaximumMessageLevel();
            throw new UPEAssertionException("ProcessField '"+elementPath+"' has unexpected maximum message level of '"+level+"' expected was '"+messageLevelError+"'.");
        }
    }

    public void assertNotHasError(String elementPath, String msgID) {
        try {
            assertHasError(elementPath, msgID);
            throw new UPEAssertionException("ProcesField '" + elementPath + "' has unexpected message '" + msgID);
        } catch( UPEAssertionException axc ) {
            return;
        }
    }

    public void assertProcessMessageQueued(String msgID) {
        TestUProcessEngine testPE = ((TestUProcessEngine)this.pc.getProcess().getProcessEngine());
        if( !testPE.containsQueuedMessage(msgID) ) {
            throw new UPEAssertionException("Expected process message'" + msgID + "' is not queued.");
        }

    }
}
