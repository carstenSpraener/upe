package upe.sample;

import upe.annotations.*;
import upe.backend.UProcessBackend;
import upe.process.UProcessEngine;
import upe.process.UProcessTextField;
import upe.process.impl.AbstractUProcessImpl;
import upe.process.messages.UProcessMessage;
import upe.process.validation.impl.MandatoryValidator;
import upe.sample.backend.UserMgr;

import java.util.HashMap;
import java.util.Map;

/**
 * Implements a small UserRegistrationProcess
 */
@UpeProcess(UserRegistrationProcess.NAME)
public class UserRegistrationProcess extends AbstractUProcessImpl {
    public static final String NAME="userRegistration";
    /**
     * Message to raise if the content of both password fields are not identically
     */
    @UpeProcessMessage(value="LOGIN-0003", level= UProcessMessage.MESSAGE_LEVEL_ERROR)
    public static final String login0003 = "Password and Password 2 are not identical.";

    /**
     * Message to raise if password is too short
     */
    @UpeProcessMessage(value="LOGIN-0004", level= UProcessMessage.MESSAGE_LEVEL_ERROR)
    public static final String login0004 = "The pasword given is to short";

    // required process fields
    @UpeProcessField("user")
    private UProcessTextField user;
    @UpeProcessField("password")
    private UProcessTextField password;
    @UpeProcessField("password2")
    private UProcessTextField password2;

    /**
     * create the Process and add Mandatory validators to the process fields.
     *
     * @param pe
     * @param name
     */
    public UserRegistrationProcess(UProcessEngine pe, String name) {
        super(pe, name);
        new MandatoryValidator("user");
        new MandatoryValidator("password");
        new MandatoryValidator("password2");
    }

    /***
     * Take the String arguments "user" and "password" and if given
     * set them to the process fields.
     *
     * @param args a maybe null map with String values for "user" and or "password"
     */
    @Override
    public void initialize(Map<String, Object> args) {
        if( args==null ) {
            return;
        }
        String userID = (String)args.get("user");
        if( userID != null ) {
            this.getProcessElement("user", UProcessTextField.class).setStringValue(userID);
        }
        String password = (String)args.get("password");
        if( userID != null ) {
            this.getProcessElement("password", UProcessTextField.class).setStringValue(password);
        }
    }

    /**
     * The process is finished. Return the user and the password to the
     * calling process.
     *
     * @return Map with the content of "user" and "password"
     */
    @Override
    public Map<String, Object> finish() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("user", getProcessElement("user", UProcessTextField.class).getStringValue());
        resultMap.put("password", getProcessElement("password", UProcessTextField.class).getStringValue());
        return resultMap;
    }

    /**
     * The process was canceled. Return nothing to the calling process.
     *
     * @return null
     */
    @Override
    public Map<String, Object> cancel() {
        return null;
    }

    /**
     * Check if there are no more errors and if no errors shown, register the
     * user in the backend and finish the process.
     * @param args
     */
    @UpeProcessAction("actRegisterOK")
    public void actRegisterUserOK(Map<String,Object> args) {
        if( getProcess().getMaximumMessageLevel() <= UProcessMessage.MESSAGE_LEVEL_ERROR ) {
            String user = getProcessElement("user", UProcessTextField.class).getStringValue();
            String password = getProcessElement("password", UProcessTextField.class).getStringValue();
            UserMgr usrMgr = UProcessBackend.getInstance().provide(UserMgr.class);
            usrMgr.registerUser(user, password);
            getProcess().getProcessEngine().finishProcess();
        }
    }

    /**
     * The process was canceled. Just inform the process engine.
     *
     * @param args
     */
    @UpeProcessAction("actRegisterCancel")
    public void actRegisterUserCancel(Map<String,Object> args) {
        getProcess().getProcessEngine().cancelProcess();
    }

    /**
     * Check that the content of password and password2 are identically if password is set.
     * @param password
     * @param password2
     * @return
     */
    @UpeValidator("LOGIN-0003")
    public boolean valPasswordsNotEqual(@UpeProcessValue("password") String password, @UpeProcessValue("password2") String password2) {
        return password==null || !password.equals(password2);
    }

    /**
     * Check that the given password is long enough.
     *
     * @param password
     * @return
     */
    @UpeValidator("LOGIN-0004")
    public boolean valPasswordsToSimple(@UpeProcessValue("password") String password) {
        return password==null || password.length()<3;
    }
}
