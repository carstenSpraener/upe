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

// Configure the processes needed by this application
@UpeApplication({
        LoginProcess.class,
        HelloWorldProcess.class,
        UserRegistrationProcess.class
})
public class LanternaApplication {
    // In the main method
    public static void main(String[] args) {
        // Read the application configuration from the annotations of this class
        ApplicationConfiguration.getInstance().readApplication(LanternaApplication.class);
        // Register a backend supplier for the user management
        UProcessBackend.addSupplier(UserMgr.NAME, () -> new UserMgrLanternaImpl());
        // Use a LanternaProcessEngine to run the application
        LanternaProcessEngine processEngine = new LanternaProcessEngine();
        // This is Lanterna-Specific code to register Panels for Processes
        processEngine.registerProcessWindow("login", ()->new LanternaLoginPanel());
        processEngine.registerProcessWindow("userRegistration", ()->new LanternaRegistrationPanel());
        processEngine.registerProcessWindow(HelloWorldProcess.NAME, ()->new LanternaHelloWorldPanel());
        // start with the login process
        processEngine.callProcess("login", null, null);
    }
}
