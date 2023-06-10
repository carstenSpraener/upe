# Testdriven Development of a Login Process with UPE

## A simple login process

The class [LoginProcess](src/main/java/upe/sample/LoginProcess.java)
defines a straightforward process with two
fields _user_ and _password_ and an action to actLogin.
Here is the key code of the LoginProcess:

```java
// This annotation names the Process as 'login'
@UpeProcess("login")
public class LoginProcess extends AbstractUProcessImpl {
    // A process field to take the users name
    @UpeProcessField
    private UProcessTextField user;
    // another process field to take the password.
    // Notice: The name of the field 'pwd' is not the name of the 
    // ProcessField 'password'
    @UpeProcessField("password")
    private UProcessTextField pwd;
    
    // Some more constructor and life cycle methods... 
    
    // A simple, not yet implemented action to do  
    // the login.
    @UpeProcessAction("actLogin")
    public void login(Map<String, Object> args) {

    }
}
```

The details are in the javadoc of the class.

## Testing a process

OK, this is just structure. Now let's define some test cases as the base of our test-driven development approach.

The full implementation of the test case is in the class
[LoginProcessTest](src/test/java/upe/sample/LoginProcessTest.java).
Here are some details.

### Setup the test environment

```java
// Use the UpeTestExtension to execute this test.
@ExtendWith({
        UpeTestExtension.class
})
// This UpeApplication consists of one process "LoginProcess"
@UpeApplication({
        LoginProcess.class
})
class LoginProcessTest {
```

### Obtaining references to the required objects
```java

    // Let the test environment inject an instance of LoginProcess into this field.
    @UpeProcessToTest("login")
    LoginProcess uut;

    // Also, let inject an instance of the running process engine
    @UInject
    TestUProcessEngine testUProcessEngine;
```

### Do some modifications and assertions
```java
    @Test
    void testLoginProcessActLoginDisabledUntilFilled() throws Exception {
        uut.initialize(new HashMap<>());
        UpeAssertions upeAssertions = new UpeAssertions(uut);

        upeAssertions.assertDisabled("/actLogin");
        // All modifications of a process need to be done in a UProcessModification block
        // This triggers the validations after the modification is finished.
        try (UProcessModification mod = new UProcessModification(uut)) {
            uut.getProcessElement("/user", UProcessTextField.class).setStringValue("johndoe");
            uut.getProcessElement("/password", UProcessTextField.class).setStringValue("password");
        }
        upeAssertions.assertEnabled("/actLogin");
    }
```
The test initializes the login process with an empty args map. It then
asserts, that the _actLogin_ is disabled.

Then, inside a modification block, it fills in the user and password fields.

As a result, the _actLogin_ should now be enabled.

## Running the tests
When you commend out the @Disabled-Annotation, you can start the test and see that all tests will fail. Greenifying these tests is the job of the
next sample-steps.

```log
LoginProcessTest > testLoginProcessNeedsUserAndPassword() FAILED
    upe.test.UPEAssertionException at LoginProcessTest.java:56
LoginProcessTest > testLoginProcessActLoginReportsPasswordToSmall() FAILED
    org.opentest4j.AssertionFailedError at LoginProcessTest.java:99
LoginProcessTest > testLoginProcessActLoginDisabledUntilFilled() FAILED
    upe.test.UPEAssertionException at LoginProcessTest.java:78
3 tests completed, 3 failed
```
