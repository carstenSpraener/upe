package upe.sample;

import upe.annotations.*;
import upe.process.UProcessAction;
import upe.process.UProcessEngine;
import upe.process.UProcessField;
import upe.process.UProcessTextField;
import upe.process.impl.AbstractUProcessImpl;
import upe.process.messages.UProcessMessage;
import upe.process.messages.UProcessMessageImpl;
import upe.process.validation.impl.MandatoryValidator;

import java.util.Map;

/**
 * A simple process for login to an application. It has two fields and a
 * login action.
 * <p>
 * Purpose: Show how to create a process, add fields and actions.
 */
// This annotation names the Process as 'login'
@UpeProcess("login")
public class LoginProcess extends AbstractUProcessImpl {
    @UpeProcessMessage(value="LOGIN-0001", level=UProcessMessage.MESSAGE_LEVEL_ERROR)
    public static final String login0001 = "The password needs to be at least 3 characters long.";

    /**
     * A simple text field to take the user id
     * The annotation lets the process engine inject a concret
     * implementation of the UProcessTextField.
     * <p>
     * The name of the field can be given to the annotation.
     * If it is left out, as in this example, it is the name
     * of the field.
     */
    @UpeProcessField
    private UProcessTextField user;
    /**
     * A second field to take the password.
     * This time the field name differs from the
     * name of the process field. Just to show how it
     * is done.
     */
    @UpeProcessField("password")
    private UProcessTextField pwd;

    /**
     * Each process needs a constructor which receives the
     * process engine, this process is running in and the name.
     * <p>
     * A concret implementation just needs to call the super
     * constructor. Additional validations could be added here.
     *
     * @param pe   The active process engine
     * @param name The name of this process.
     */
    public LoginProcess(UProcessEngine pe, String name) {
        super(pe, name);
        addValidator(new MandatoryValidator("user"));
        addValidator(new MandatoryValidator("password"));
    }

    /**
     * This is one of the three live cycle methods of  a process. The
     * args map holds the start values. Typically, this initialization
     * takes the arguments, retrieves some data from them and fills in
     * the process fields. But for now its OK to keep it empty.
     *
     * @param args
     */
    @Override
    public void initialize(Map<String, Object> args) {
    }

    /**
     * The process is finished. The args returned by this
     * method will be given to a potential "return action" from
     * a calling process. This process is a root, so it is not
     * called from any other process and does not need to return
     * any results.
     *
     * @return
     */
    @Override
    public Map<String, Object> finish() {
        return null;
    }

    /**
     * The process was canceled. This args returned by this method
     * will be given to the "return action" from the calling process.
     * <p>
     * Since this process is a root process it don't need to return
     * anything.
     *
     * @return
     */
    @Override
    public Map<String, Object> cancel() {
        return null;
    }

    /**
     * This shows how to implement a action in a convenient way as a method
     * on a process component.
     *
     * The Action can be called via its name "actLogin"
     * @param args
     */
    @UpeProcessAction("actLogin")
    public void login(Map<String, Object> args) {

    }

    @UpeValidator("LOGIN-0001")
    public boolean valPasswordToShort(@UpeProcessValue("password") String pwd) {
        return pwd==null ||pwd.length()<3;
    }

    /**
     * If there is no more error on the process, the actLogin can be enabled. If there is an error, it should
     * be disabled.
     */
    @UpeRule
    public void rulEnableActLogin(@UpeProcessValue("user") UProcessField user, @UpeProcessValue("password") UProcessField password) {
        UProcessAction actLogin = getProcessElement("actLogin", UProcessAction.class);
        boolean isEnabled = user.getMaximumMessageLevel() <= UProcessMessage.MESSAGE_LEVEL_WARNING &&
                password.getMaximumMessageLevel() <= UProcessMessage.MESSAGE_LEVEL_WARNING;
        actLogin.setEnabled(isEnabled);
    }
}
