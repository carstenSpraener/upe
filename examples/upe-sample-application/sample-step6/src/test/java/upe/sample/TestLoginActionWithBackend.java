package upe.sample;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import upe.annotations.UpeApplication;
import upe.process.UProcessAction;
import upe.process.UProcessField;
import upe.process.UProcessModification;
import upe.sample.backend.UserMgr;
import upe.test.TestUProcessEngine;
import upe.test.UpeAssertions;
import upe.test.annotations.UInject;
import upe.test.annotations.UpeBackendComponent;
import upe.test.annotations.UpeProcessToTest;
import upe.test.jupiter.UpeTestExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        HelloWorldProcess.class
})
public class TestLoginActionWithBackend {

    // Let the test environment inject a instance of LoginProcess to this field.
    @UpeProcessToTest("login")
    LoginProcess uut;

    // Also let inject an instance of the running process engine
    @UInject
    TestUProcessEngine testUProcessEngine;

    @UpeBackendComponent(UserMgr.NAME)
    UserMgr usrMgrMock = mock(UserMgr.class);

    @BeforeEach
    void setupUsrMgrMock() {
        when(usrMgrMock.isLoginCorrect(any(), any())).thenAnswer( i -> {
            String user = i.getArgument(0);
            String password = i.getArgument(1);

            return "johndoe".equals(user) && "password".equals(password);
        });
    }

    /**
     * Test the login action with illegal credentials. A message LOGIN-0002
     * should be queued.
     */
    @Test
    void testUnsuccessfulLogin() {
        // Set the user and password field into the login process
        try(UProcessModification mod = new UProcessModification(uut)) {
            uut.getProcessElement("user", UProcessField.class).setValueFromFrontend("nixon");
            uut.getProcessElement("password", UProcessField.class).setValueFromFrontend("watergate");
        }
        // call the login action
        uut.getProcessElement("actLogin", UProcessAction.class).execute(null);
        // check that the LOGIN-0002 message is queued.
        new UpeAssertions(uut).assertProcessMessageQueued("LOGIN-0002");
    }

    /**
     * Test a successful login and check that the active process is HelloWorld
     * and that the content is set correctly.
     */
    @Test
    void testSuccessfulLogin() {
        // provide correct credentials
        try(UProcessModification mod = new UProcessModification(uut)) {
            uut.getProcessElement("user", UProcessField.class).setValueFromFrontend("johndoe");
            uut.getProcessElement("password", UProcessField.class).setValueFromFrontend("password");
        }
        // trigger the login action
        uut.getProcessElement("actLogin", UProcessAction.class).execute(null);
        // assert that the HelloWorld process is active
        assertTrue( testUProcessEngine.getActiveProcess() instanceof HelloWorldProcess);
        // and that the content field contains correct text.
        new UpeAssertions(testUProcessEngine.getActiveProcess())
            .assertHasValue("content", "Hello to 'johndoe'");
    }
}
