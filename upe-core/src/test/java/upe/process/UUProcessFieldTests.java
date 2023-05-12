package upe.process;

import org.junit.Test;
import upe.process.impl.UProcessBooleanFieldImpl;
import upe.process.impl.UProcessTextFieldImpl;
import upe.process.messages.UProcessMessage;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UUProcessFieldTests {
    UProcessComponent pc = mock(UProcessComponent.class);

    @Test
    public void testProcessTextFieldMessageHandling() throws Exception {
        UProcessTextFieldImpl txtField = new UProcessTextFieldImpl(pc, "test");
        txtField.setValue("Hello World");
        UProcessMessage msgError = mock(UProcessMessage.class);
        when(msgError.getMessageLevel()).thenReturn(UProcessMessage.MESSAGE_LEVEL_ERROR);
        when(msgError.getMessageID()).thenReturn("ID");
        when(msgError.getMessageText()).thenReturn("A Error Message");

        UProcessMessage msgWarn = mock(UProcessMessage.class);
        when(msgWarn.getMessageLevel()).thenReturn(UProcessMessage.MESSAGE_LEVEL_WARNING);
        when(msgWarn.getMessageID()).thenReturn("ID-2");
        when(msgWarn.getMessageText()).thenReturn("A Warning Message");

        txtField.addProcessMessage(msgError);
        txtField.addProcessMessage(msgWarn);
        assertEquals(UProcessMessage.MESSAGE_LEVEL_ERROR, txtField.getMaximumMessageLevel());

        txtField.removeProcessMessage(msgError);
        assertEquals(UProcessMessage.MESSAGE_LEVEL_WARNING, txtField.getMaximumMessageLevel());

        txtField.removeProcessMessage(msgWarn);
        assertEquals(UProcessMessage.MESSAGE_LEVEL_NONE, txtField.getMaximumMessageLevel());
    }

    @Test
    public void testProcessBooleanField() throws Exception {
        UProcessBooleanFieldImpl booleanField = new UProcessBooleanFieldImpl(pc, "testJN");

        booleanField.setBooleanValue(null);
        assertNull(booleanField.getBooleanValue());
        booleanField.setValueFromFrontend("");
        assertNull(booleanField.getBooleanValue());

        booleanField.setBooleanValue(Boolean.FALSE);
        assertEquals(Boolean.FALSE, booleanField.getBooleanValue());
        booleanField.setBooleanValue(Boolean.TRUE);
        assertTrue(booleanField.getBooleanValue());

        for( String str : new String[] {"ja", "Ja", "JA","J","Y","YES", "true", "1"} ){
            booleanField.setBooleanValue(Boolean.FALSE);
            booleanField.setValueFromFrontend(str);
            assertTrue(booleanField.getBooleanValue());
        }
    }
}
