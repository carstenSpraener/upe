package upe.sample.lanterna;

import upe.annotations.UpeApplication;
import upe.backend.UProcessBackend;
import upe.binding.lanterna.LanternaProcessEngine;
import upe.process.ApplicationConfiguration;
import upe.sample.HelloWorldProcess;
import upe.sample.LoginProcess;
import upe.sample.UserRegistrationProcess;
import upe.sample.backend.UserMgr;
import upe.sample.lanterna.backend.UserMgrLanternaImpl;

@UpeApplication({
        LoginProcess.class,
        HelloWorldProcess.class,
        UserRegistrationProcess.class
})
public class LanternaApplication {

    public static void main(String[] args) {
        ApplicationConfiguration.getInstance().readApplication(LanternaApplication.class);
        UProcessBackend.addSupplier(UserMgr.NAME, () -> new UserMgrLanternaImpl());

        LanternaProcessEngine processEngine = new LanternaProcessEngine();
        processEngine.registerProcessWindow("login", ()->new LanternaLoginPanel());
        processEngine.registerProcessWindow("userRegistration", ()->new LanternaRegistrationPanel());
        processEngine.registerProcessWindow(HelloWorldProcess.NAME, ()->new LanternaHelloWorldPanel());

        processEngine.callProcess("login", null, null);
    }
}
