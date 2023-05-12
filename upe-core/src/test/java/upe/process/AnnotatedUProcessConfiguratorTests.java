package upe.process;

import org.junit.Test;
import upe.process.testapp.Application;
import upe.process.testapp.DerivedUProcess;
import upe.process.engine.BaseUProcessEngine;

import java.util.HashMap;

import static org.junit.Assert.*;

public class AnnotatedUProcessConfiguratorTests {

    /**
     * This tests the behaviour for field inheritance. The derived class overwrites a
     * field of the base class.
     *
     * @throws Exception
     */
    @Test
    public void testFieldInheritance() throws Exception {
        UProcess activeUProcess = createTestProcess();
        assertNotNull(activeUProcess);
        assertTrue( activeUProcess instanceof DerivedUProcess);
        assertTrue(activeUProcess.getProcessElement("/name") instanceof UProcessTextField);
        // This is the difference between accessing a field directly or via a path.
        assertEquals("Hallo", ((UProcessTextField) activeUProcess.getProcessElement("/name")).getValueForFrontend());
        assertEquals("Base-Hallo", ((DerivedUProcess) activeUProcess).getNameValueDirect());
    }

    public static UProcess createTestProcess() {
        ApplicationConfiguration.getInstance().readApplication(Application.class);
        BaseUProcessEngine engine = new BaseUProcessEngine();
        engine.callProcess("TestProcess", new HashMap<>(), null);

        UProcess activeUProcess = engine.getActiveProcessInfo().getProcess();
        return activeUProcess;
    }
}
