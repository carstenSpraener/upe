# Testdriven Development of a Login Process with UPE

That was a solid trip till here. Now it is time to complete the
user login process and fullfill it with a tiny registration
process. The user can call a ActRegistration, fill in the
data, activate the ActRegistrationOK action and can 
successfully log in to the application.

### What you will learn in this step

* Calling a sub process form a process
* Providing data to the sub process
* Retrieving data, when the process returns 
* Handling cancel of a called process

## Define a test case for the _nice weather flight_

This time the test case will walk through the whole application with the
following steps:

* Enter user and password into the login process
* activate the action actRegistration
* fill in the password and password2 field with identical values
* activate the actRegistrationOK action
* activate the actLogin action
* verify that the HelloWorldProcess is running with correct input.

This is a more complex test and shows how a UPE application can be 
tested completely with JUnit.

### The test class

```java
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
        uut.initialize(new HashMap<>());
        try (UProcessModification mod = new UProcessModification(uut)) {
            uut.getProcessElement("user", UProcessTextField.class).setStringValue("johndoe");
            uut.getProcessElement("password", UProcessTextField.class).setStringValue("p");
        }
        uut.getProcessElement("actRegister", UProcessAction.class).execute(null);
        assertTrue( testUProcessEngine.getActiveProcess() instanceof UserRegistrationProcess );
        UserRegistrationProcess registrationProcess = (UserRegistrationProcess) testUProcessEngine.getActiveProcess();
        new UpeAssertions(testUProcessEngine.getActiveProcess())
                .assertHasValue("user", "johndoe")
                .assertHasValue("password", "p")
                .assertHasError("password", "LOGIN-0004")
                .assertHasError("password", "LOGIN-0003")
                .assertHasError("password2", "LOGIN-0003")
        ;
        try(UProcessModification mod = new UProcessModification(registrationProcess)) {
            registrationProcess.getProcessElement("password", UProcessTextField.class).setStringValue("password");
            registrationProcess.getProcessElement("password2", UProcessTextField.class).setStringValue("password");
        }
        new UpeAssertions(testUProcessEngine.getActiveProcess())
                .assertNotHasError("password", "LOGIN-0003")
                .assertNotHasError("password", "LOGIN-0004")
                .assertNotHasError("password2", "LOGIN-0004")
        ;
        registrationProcess.getProcessElement("actRegisterOK", UProcessAction.class).execute(null);
        assertTrue( testUProcessEngine.getActiveProcess() instanceof LoginProcess );
        assertTrue( uut == testUProcessEngine.getActiveProcess());

        new UpeAssertions(uut)
                .assertHasValue("user", "johndoe")
                .assertHasValue("password", "password")
        ;
        uut.getProcessElement("actLogin", UProcessAction.class).execute(null);
        assertTrue( testUProcessEngine.getActiveProcess() instanceof HelloWorldProcess );
        HelloWorldProcess helloWorldProcess = (HelloWorldProcess)testUProcessEngine.getActiveProcess();
        new UpeAssertions(helloWorldProcess)
                .assertHasValue("content", "Hello to 'johndoe'");
    }

}
```

## Greenify the _Nice weather flight_

### Implement a new UserRegistrationProcess

With the knowledge of the previous steps this should be done quite easy and 
straight forward.

```java

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
```

## Takeaways

In this last section of process implementation you learned:

* Calling a sub process
* Providing data to a sub process
* Giving data back to a calling process and receive them
* Writing a test that walks to a whole complex scenario
