package upe.test;

import upe.process.UProcessComponent;

/**
 * Helper to make expressing assertions in UPE easy. See documentation of the
 * assert-methods for details.
 */
public class UpeAssertions {
    private UProcessComponent pc;

    /**
     * Create an assertion for a UProcessComponent instance.
     * @param pc the UProcessComponent-instance to assert
     */
    public UpeAssertions(UProcessComponent pc) {
        this.pc = pc;
    }

    /**
     * Check that a given UProcessElement in the UProcessComponent has an error with the given ID.
     * @param elementPath the path to the element that should hold the error.
     * @param msgID the msgID of the error.
     */
    public UpeAssertions assertHasError(String elementPath, String msgID) {
        String containedMessageID = pc.getProcessElement(elementPath).getMessages().stream()
                .filter(msg -> msg.getMessageID().equals(msgID))
                .map(msg->msg.getMessageID())
                .findFirst().orElse(null);
        if( containedMessageID==null ) {
            throw new UPEAssertionException("ProcessField '"+elementPath+"' does not contain message with id '"+msgID+"'.");
        }
        return this;
    }

    /**
     * Check that the requested UProcessElement in the UProcessComponent has a maximum message error level
     * as given.
     * @param elementPath The path to the element that should be asserted
     * @param messageLevelError the messageLevel expected.
     */
    public UpeAssertions assertMaxMsgLevel(String elementPath, int messageLevelError) {
        if( messageLevelError != this.pc.getProcessElement(elementPath).getMaximumMessageLevel() ) {
            int level = this.pc.getProcessElement(elementPath).getMaximumMessageLevel();
            throw new UPEAssertionException("ProcessField '"+elementPath+"' has unexpected maximum message level of '"+level+"' expected was '"+messageLevelError+"'.");
        }
        return this;
    }

    /**
     * Check that a given UProcessElement in the UProcessComponent does not have an error with the given ID.
     * @param elementPath the path to the element that should hold the error.
     * @param msgID the msgID of the error.
     */
    public UpeAssertions assertNotHasError(String elementPath, String msgID) {
        try {
            assertHasError(elementPath, msgID);
            throw new UPEAssertionException("ProcesField '" + elementPath + "' has unexpected message '" + msgID);
        } catch( UPEAssertionException axc ) {
        }
        return this;
    }

    /**
     * Check that during processing a message with the given msgID was queued.
     * @param msgID The id of the message that should have been queued.
     */
    public UpeAssertions assertProcessMessageQueued(String msgID) {
        TestUProcessEngine testPE = ((TestUProcessEngine)this.pc.getProcess().getProcessEngine());
        if( !testPE.containsQueuedMessage(msgID) ) {
            throw new UPEAssertionException("Expected process message'" + msgID + "' is not queued.");
        }
        return this;
    }

    public UpeAssertions assertDisabled(String elementPath) {
        if( this.pc.getProcess().getProcessElement(elementPath).isEnabled() ) {
            throw new UPEAssertionException("Expected process element "+elementPath+" to be disable, but it is enabled.");
        }
        return this;
    }

    public UpeAssertions assertEnabled(String elementPath) {
        if( !this.pc.getProcess().getProcessElement(elementPath).isEnabled() ) {
            throw new UPEAssertionException("Expected process element "+elementPath+" to be enabled, but it is disabled.");
        }
        return this;
    }
}
