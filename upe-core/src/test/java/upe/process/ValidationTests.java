package upe.process;

import org.junit.Test;
import upe.process.testapp.Application;
import upe.process.validation.impl.MandantoryValidator;

import static org.junit.Assert.assertEquals;

public class ValidationTests {


    @Test
    public void testValidation() throws Exception {
        UProcess activeUProcess = Application.createTestProcess();

        MandantoryValidator validator = new MandantoryValidator("/name");
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
    }
}
