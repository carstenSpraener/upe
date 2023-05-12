package upe.process;

import org.junit.Test;
import upe.process.engine.BaseUProcessEngine;
import upe.process.testapp.*;
import upe.process.testapp.DerivedUProcess;
import upe.process.testapp.SecondUProcess;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UProcessNavigationTest {

    @Test
    public void testJump2Process() throws Exception {
        UProcess activeUProcess = Application.createTestProcess();
        activeUProcess.getProcessEngine().jumpToProcess("SecondProcess",
                new HashMap<>()
        );
        BaseUProcessEngine procEngine = (BaseUProcessEngine) activeUProcess.getProcessEngine();
        UProcess nextUProcess = procEngine.getActiveProcessInfo().getProcess();

        assertTrue( nextUProcess instanceof SecondUProcess);
    }

    @Test
    public void testCallProcess() {
        UProcess activeUProcess = Application.createTestProcess();
        BaseUProcessEngine baseEngine = (BaseUProcessEngine) activeUProcess.getProcessEngine();

        UProcessAction act = (UProcessAction) activeUProcess.getProcessElement("/actCallSubprocess");
        act.execute(null);

        activeUProcess = baseEngine.getActiveProcessInfo().getProcess();
        assertTrue(activeUProcess instanceof SubUProcess);
        UProcessAction actClose = (UProcessAction) activeUProcess.getProcessElement("/actClose");
        actClose.execute(null);

        activeUProcess = baseEngine.getActiveProcessInfo().getProcess();
        assertTrue(activeUProcess instanceof DerivedUProcess);
        String resultValue = ((DerivedUProcess) activeUProcess).getFieldValue("/result");
        assertEquals("FINISHED", resultValue);
    }

    @Test
    public void testCallProcessAndCancle() {
        UProcess activeUProcess = Application.createTestProcess();
        BaseUProcessEngine baseEngine = (BaseUProcessEngine) activeUProcess.getProcessEngine();

        UProcessAction act = (UProcessAction) activeUProcess.getProcessElement("/actCallSubprocess");
        act.execute(null);

        activeUProcess = baseEngine.getActiveProcessInfo().getProcess();
        assertTrue(activeUProcess instanceof SubUProcess);
        UProcessAction actClose = (UProcessAction) activeUProcess.getProcessElement("/actCancel");
        actClose.execute(null);

        activeUProcess = baseEngine.getActiveProcessInfo().getProcess();
        assertTrue(activeUProcess instanceof DerivedUProcess);
        String resultValue = ((DerivedUProcess) activeUProcess).getFieldValue("/result");
        assertEquals("CANCELED", resultValue);
    }
}
