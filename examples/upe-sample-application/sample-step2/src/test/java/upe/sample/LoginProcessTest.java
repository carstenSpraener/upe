package upe.sample;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import upe.annotations.UpeApplication;
import upe.process.UProcessAction;
import upe.process.UProcessModification;
import upe.process.UProcessTextField;
import upe.process.messages.UProcessMessage;
import upe.test.TestUProcessEngine;
import upe.test.UpeAssertions;
import upe.test.annotations.UInject;
import upe.test.annotations.UpeProcessToTest;
import upe.test.jupiter.UpeTestExtension;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * This test check the behaviour of the LoginProcess.
 * <p>
 * Purpose: Demonstrate how to write test cases with the UpeTestExtension,
 * bind a process to the test, set values, trigger actions and check the
 * state of process elements.
 */
// Use the UpeTestExtension to execute this test.
@ExtendWith({
        UpeTestExtension.class
})
// This UpeApplication consists of one process "LoginProcess"
@UpeApplication({
        LoginProcess.class
})
// Till now there is nothing implemented. Just the structure was defined. SO
// all tests will be red. Disable them to not break the build.
class LoginProcessTest {

    // Let the test environment inject a instance of LoginProcess to this field.
    @UpeProcessToTest("login")
    LoginProcess uut;

    // Also let inject an instance of the running process engine
    @UInject
    TestUProcessEngine testUProcessEngine;
    /**
     * Test that both fields a mandatory.
     *
     * @throws Exception
     */
    @Test
    void testLoginProcessNeedsUserAndPassword() throws Exception {
        uut.initialize(new HashMap<>());
        UpeAssertions upeAssertions = new UpeAssertions(uut);
        upeAssertions.assertMaxMsgLevel("/user", UProcessMessage.MESSAGE_LEVEL_ERROR);
        upeAssertions.assertMaxMsgLevel("/password", UProcessMessage.MESSAGE_LEVEL_ERROR);
    }

    /**
     * the action login should be disabled at the start. When user and password
     * are filled in, the action should be enabled.
     *
     * @throws Exception
     */
    @Disabled("This tests are disabled, since sample-step3")
    @Test
    void testLoginProcessActLoginDisabledUntilFilled() throws Exception {
        uut.initialize(new HashMap<>());
        UpeAssertions upeAssertions = new UpeAssertions(uut);

        upeAssertions.assertDisabled("/actLogin");
        // All modifications of a process need to be done in a UProcessModification block
        // This triggers the validations after modification is finished.
        try (UProcessModification mod = new UProcessModification(uut)) {
            uut.getProcessElement("/user", UProcessTextField.class).setStringValue("johndoe");
            uut.getProcessElement("/password", UProcessTextField.class).setStringValue("password");
        }
        upeAssertions.assertEnabled("/actLogin");
    }

    /**
     * The password is too short. This should result in an error on the password field.
     *
     * @throws Exception
     */
    @Test
    void testLoginProcessActLoginReportsPasswordToShort() throws Exception {
        uut.initialize(new HashMap<>());
        UpeAssertions upeAssertions = new UpeAssertions(uut);

        try (UProcessModification mod = new UProcessModification(uut)) {
            uut.getProcessElement("/user", UProcessTextField.class).setStringValue("johndoe");
            uut.getProcessElement("/password", UProcessTextField.class).setStringValue("p");
        }
        upeAssertions.assertHasError("/password", "LOGIN-0001");
    }

}
