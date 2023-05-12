package upe.process.testapp;

import upe.annotations.UpeApplication;
import upe.process.ApplicationConfiguration;
import upe.process.UProcess;
import upe.process.engine.BaseUProcessEngine;

import java.util.HashMap;

@UpeApplication({
        DerivedUProcess.class,
        SecondUProcess.class,
        SubUProcess.class
})
public class Application {

    public static UProcess createTestProcess() {
        ApplicationConfiguration.getInstance().readApplication(Application.class);
        BaseUProcessEngine engine = new BaseUProcessEngine();
        engine.callProcess("TestProcess", new HashMap<>(), null);

        UProcess activeUProcess = engine.getActiveProcessInfo().getProcess();
        return activeUProcess;
    }
}
