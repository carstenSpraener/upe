package upe.sample;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import upe.annotations.UpeApplication;
import upe.process.UProcessAction;
import upe.process.UProcessModification;
import upe.process.UProcessTextField;
import upe.sample.backend.UserMgr;
import upe.test.TestUProcessEngine;
import upe.test.UpeAssertions;
import upe.test.annotations.UInject;
import upe.test.annotations.UpeBackendComponent;
import upe.test.annotations.UpeProcessToTest;
import upe.test.jupiter.UpeTestExtension;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith({
        UpeTestExtension.class
})
// This UpeApplication consists of one process "LoginProcess"
@UpeApplication({
        LoginProcess.class,
        HelloWorldProcess.class,
        UserRegistrationProcess.class
})
public class TestUserRegistration {

    // Let the test environment inject a instance of LoginProcess to this field.
    @UpeProcessToTest("login")
    LoginProcess uut;

    // Also let inject an instance of the running process engine
    @UInject
    TestUProcessEngine testUProcessEngine;

    @UpeBackendComponent(UserMgr.NAME)
    UserMgr usrMgrMock = mock(UserMgr.class);

    Set<String> userRegistration = new HashSet<>();

    @BeforeEach
    void setupUsrMgrMock() {
        // The mock now has some very basic user registration
        when(usrMgrMock.isLoginCorrect(any(), any())).thenAnswer( i -> {
            String user = i.getArgument(0);
            String password = i.getArgument(1);
            return userRegistration.contains(user+"::"+password);
        });
        when(usrMgrMock.registerUser(any(), any())).thenAnswer( i -> {
            String user = i.getArgument(0);
            String password = i.getArgument(1);
            userRegistration.add(user+"::"+password);
            return user;
        });
    }

    @Test
    void testUserRegistrationAndLogin() {
        // start the login process
        uut.initialize(new HashMap<>());
        // fill in the fields
        try (UProcessModification mod = new UProcessModification(uut)) {
            uut.getProcessElement("user", UProcessTextField.class).setStringValue("johndoe");
            uut.getProcessElement("password", UProcessTextField.class).setStringValue("p");
        }
        // activate the actRegister action
        uut.getProcessElement("actRegister", UProcessAction.class).execute(null);
        // check that the actual active process is the registration process
        assertTrue( testUProcessEngine.getActiveProcess() instanceof UserRegistrationProcess );
        UserRegistrationProcess registrationProcess = (UserRegistrationProcess) testUProcessEngine.getActiveProcess();
        // check that the process received the data and that the validations are applied
        new UpeAssertions(testUProcessEngine.getActiveProcess())
                .assertHasValue("user", "johndoe")
                .assertHasValue("password", "p")
                .assertHasError("password", "LOGIN-0004")
                .assertHasError("password", "LOGIN-0003")
                .assertHasError("password2", "LOGIN-0003")
        ;
        // fill in the registration fields with valid values
        try(UProcessModification mod = new UProcessModification(registrationProcess)) {
            registrationProcess.getProcessElement("password", UProcessTextField.class).setStringValue("password");
            registrationProcess.getProcessElement("password2", UProcessTextField.class).setStringValue("password");
        }
        // check that all messages are removed
        new UpeAssertions(testUProcessEngine.getActiveProcess())
                .assertNotHasError("password", "LOGIN-0003")
                .assertNotHasError("password", "LOGIN-0004")
                .assertNotHasError("password2", "LOGIN-0004")
        ;
        // activate the actRegisterOK action
        registrationProcess.getProcessElement("actRegisterOK", UProcessAction.class).execute(null);
        // Now the active process should be the HelloWorldProcess
        assertTrue(testUProcessEngine.getActiveProcess() instanceof HelloWorldProcess );
        // and the content should be set as expected
        new UpeAssertions(testUProcessEngine.getActiveProcess())
                .assertHasValue("content", "Hello to 'johndoe'");
    }

}
