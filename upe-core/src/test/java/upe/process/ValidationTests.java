package upe.process;

import org.junit.Test;
import upe.process.testapp.Application;
import upe.process.validation.impl.MandatoryValidator;

import static org.junit.Assert.assertEquals;

public class ValidationTests {


    @Test
    public void testValidation() throws Exception {
        UProcess activeUProcess = Application.createTestProcess();

        MandatoryValidator validator = new MandatoryValidator("/name");
        activeUProcess.addValidator(validator);
        UProcessField nameElement = (UProcessField) activeUProcess.getProcessElement("/name");

        try(UProcessModification m = new UProcessModification(activeUProcess) ) {
            nameElement.setValue(null);
        }
        assertEquals(3, nameElement.getMaximumMessageLevel());

        try(UProcessModification m = new UProcessModification(activeUProcess) ) {
            nameElement.setValueFromFrontend("Carsten");
        }
        assertEquals(0, nameElement.getMaximumMessageLevel());
        // And when we come this long... check that the rule was executed.
        assertEquals("carsten", activeUProcess.getProcessElement("/myNameLC", UProcessField.class).getValue());
    }
}
