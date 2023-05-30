package upe.demo.process;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import upe.annotations.UpeApplication;
import upe.demo.process.backend.PersonMgr;
import upe.demo.rest.component.PersonMgrImpl;
import upe.process.UProcess;
import upe.process.UProcessEngine;
import upe.process.UProcessField;
import upe.test.TestUProcessEngine;
import upe.test.annotations.UInject;
import upe.test.annotations.UpeBackendComponent;
import upe.test.annotations.UpeProcessToTest;
import upe.test.jupiter.UpeTestExtension;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(
        UpeTestExtension.class
)
@UpeApplication({
        PersonProcess.class,
        ClientBrowsingProcess.class,
        ClientBrowserDispatcherProcess.class
})
public class TestDispatcherProcess {
    @UpeBackendComponent("personMgr")
    private PersonMgr personMgr = mock(PersonMgr.class);

    @UInject
    private TestUProcessEngine processEngine;

    @BeforeEach
    public void setSupplier() {
        when(personMgr.findAll()).thenReturn(new PersonMgrImpl().findAll());
    }

    @Test
    public void testEnterWithNoArgs() throws Exception {
        this.processEngine.callProcess("clientBrowserDispatcherProcess", new HashMap<>(), null);
        assertEquals("clientBrowsingProcess", this.processEngine.getActiveProcessInfo().getProcess().getName());
        UProcess p =  this.processEngine.getActiveProcessInfo().getProcess();
        UProcessField nameField = (UProcessField)p.getProcess().getProcessElement("/personList[0]/firstName");
        assertEquals("Carsten", nameField.getValue());
        assertEquals("/personList[0]/firstName", nameField.getElementPath());
    }
}
