package upe.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import upe.annotations.UpeApplication;
import upe.process.UProcessEngine;
import upe.process.UProcessModification;
import upe.process.UProcessTextField;
import upe.process.messages.UProcessMessage;
import upe.process.validation.impl.MandatoryValidator;
import upe.test.annotations.UInject;
import upe.test.annotations.UpeBackendComponent;
import upe.test.annotations.UpeProcessToTest;
import upe.test.jupiter.UpeTestExtension;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(
        UpeTestExtension.class
)
@UpeApplication(
        ASimpleProcess.class
)
class TestUProcessEngineTest {

    @UInject
    private UProcessEngine processEngine;

    @UpeProcessToTest(ASimpleProcess.NAME)
    private ASimpleProcess uut;

    @UpeBackendComponent(BackendService.NAME)
    private BackendService stringProvider = mock(BackendService.class);

    @BeforeEach
    public void setup() {
        when(stringProvider.get()).thenReturn("Hello World");
    }

    @Test
    void testProcessSetup() {
        assertNotNull(uut);
        assertNotNull(processEngine);
        try(UProcessModification pm = new UProcessModification(uut)) {
            uut.initialize(new HashMap<>());
        }
        assertEquals("Hello World", uut.getProcessElement("/name", UProcessTextField.class).getStringValue());
    }

    @Test
    void testNoNameRaisesError() throws Exception {
        // Initialize the process and set the name value to empty string
        try(UProcessModification pm = new UProcessModification(uut)) {
            uut.initialize(new HashMap<>());
            uut.setFieldValue("name", "");
        }
        // check that the expected error occurred
        new UpeAssertions(uut)
                .assertHasError("/name", MandatoryValidator.MSG_ID)
                .assertMaxMsgLevel("/name", UProcessMessage.MESSAGE_LEVEL_ERROR)
        ;
        // modify the name field and set some value
        try(UProcessModification pm = new UProcessModification(uut)) {
            uut.setFieldValue("name", "Moin!");
        }
        // check that the expected error disappeared.
        new UpeAssertions(uut)
                .assertNotHasError("/name",  MandatoryValidator.MSG_ID)
                .assertMaxMsgLevel("/name", UProcessMessage.MESSAGE_LEVEL_NONE)
                ;
    }
}
