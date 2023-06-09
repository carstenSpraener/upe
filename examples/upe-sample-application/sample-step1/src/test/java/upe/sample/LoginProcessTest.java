package upe.sample;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;
import org.junit.jupiter.api.extension.ExtendWith;
import upe.annotations.UpeApplication;
import upe.process.UProcessAction;
import upe.process.UProcessTextField;
import upe.process.messages.UProcessMessage;
import upe.test.TestUProcessEngine;
import upe.test.UpeAssertions;
import upe.test.annotations.UInject;
import upe.test.annotations.UpeProcessToTest;
import upe.test.jupiter.*;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({
        UpeTestExtension.class
})
@UpeApplication({
        LoginProcess.class
})
@Disabled("This tests are disabled, since sample-step2")
class LoginProcessTest {

    @UpeProcessToTest("login")
    LoginProcess uut;

    @Test
    void testLoginProcessNeedsUserAndPassword() throws Exception {
        uut.initialize(new HashMap<>());
        UpeAssertions upeAssertions = new UpeAssertions(uut);
        upeAssertions.assertMaxMsgLevel("/user", UProcessMessage.MESSAGE_LEVEL_ERROR);
        upeAssertions.assertMaxMsgLevel("/pwd1", UProcessMessage.MESSAGE_LEVEL_ERROR);
    }

    @Test
    void testLoginProcessActLoginDisabledUntilFilled() throws Exception {
        uut.initialize(new HashMap<>());
        UpeAssertions upeAssertions = new UpeAssertions(uut);

        upeAssertions.assertDisabled("/actLogin");
        uut.getProcessElement("/user", UProcessTextField.class).setStringValue("johndoe");
        uut.getProcessElement("/pwd1", UProcessTextField.class).setStringValue("password");
        upeAssertions.assertEnabled("/actLogin");
    }

    @Test
    void testLoginProcessActLoginReportsPasswordToSmall() throws Exception {
        uut.initialize(new HashMap<>());
        UpeAssertions upeAssertions = new UpeAssertions(uut);

        uut.getProcessElement("/user", UProcessTextField.class).setStringValue("johndoe");
        uut.getProcessElement("/pwd1", UProcessTextField.class).setStringValue("p");
        uut.getProcessElement("actLogin", UProcessAction.class).execute(null);
        //
        upeAssertions.assertProcessMessageQueued("LOGIN001");
    }

}
