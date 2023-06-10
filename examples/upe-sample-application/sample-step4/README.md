# Testdriven Development of a Login Process with UPE

## What you will learn in this step

* Calling backend logic from a UProcessComponent
* Creating another UProcess
* Mocking the backend in the test environment
* Jumping to another process and providing some parameter

## Calling Backend logic from a UProcessComponent

At some point, the process needs to send data to and retrieve data from some
kind of backend. A UPE application is intended to run in many different environments, so the backend could vary in each domain. From stand-alone installation to cloud space deployment, it should be able to run. To achieve this goal, there need to be some restrictions to a backend facade.

* The parameters and results of a backend method must be serializable in some way.
  Json could be a good choice, but it depends on your implementation.
* Each method provided by the interface runs in its own database transaction. This
  is especially important when the backends are running on physically different machines.
  Otherwise, the action has to deal with distributed transactions. Do so if you want. But believe me, it is better to avoid this. Even if the number of methods on the interfaces exceeds.

You can overcome this restriction by using DTOs as parameters and results and by orchestrating a method call sequence into a new interface method. But for our example, the world is relatively easy. So let's get started.

### Create an interface holding the necessary methods

When the user enters a username and a password, the action login should send the data to a backend and get a boolean showing if the data is correct. So the following interface can reflect these requirements:

```java
@UpeBackendFacade(UserMgr.NAME)
public interface UserMgr {
    String NAME = "userMgr";

    Boolean isLoginCorrect(String user, String password);
}
```

The interface is in the package `backend`. Notice that it is annotated with a
`@UpeBackendFacade` annotation and with a name.

### Provide a mock in the test environment and register it in UPE

The tests for this requirement will be in a new class
[TestLoginActionWithBackend](src/test/java/upe/sample/TestLoginActionWithBackend.java).

In the test environment, there must be some mock of the UserMgr interface to test the process.
This mock is registered to the UPE application with the `@UpeBackendComponent` annotation
on the field holding the mock:

```java
    @UpeBackendComponent(UserMgr.NAME)
    UserMgr usrMgrMock = mock(UserMgr.class);
```
In a `@BeforeEach` method, the behavior of the mock is defined. It allows user
_johndoe_ with password _password_ to log in. All other request causes a false.

```java
    @BeforeEach
    void setupUsrMgrMock() {
        when(usrMgrMock.isLoginCorrect(any(), any())).thenAnswer( i -> {
            String user = i.getArgument(0);
            String password = i.getArgument(1);
            
            return "johndoe".equals(user) && "password".equals(password);
        });
    }
```

That's it for now. The application defines a backend facade for requesting login results, and in the test, a mock for the UserMgr facade is provided.

Now let's go on to the following requirement.

## Define a second process
On successful login, the application has to jump to another process, which starts the actual application. In this tiny example, it is a simple `HelloWorldProcess` which takes the username as an argument and sets a message text to its content field.

With the knowledge of the previous steps, it should be easy to understand the code in [HelloWorldProcess.java](src/main/java/upe/sample/HelloWorldProcess.java)

Here are the essentials:
```java
// Define a new process "helloWorld"
@UpeProcess("helloWorld")
public class HelloWorldProcess extends AbstractUProcessImpl {
    public static final String NAME = "helloWorld";

    // Define a field to hold the content 
    @UpeProcessField("content")
    private UProcessTextField content;
    // ... some constructor and live cycle methods

    // In the initialize method take the user from the arguments
    // and set the content text.
    @Override
    public void initialize(Map<String, Object> args) {
        if (args.get("user") != null) {
            content.setStringValue("Hello to '" + args.get("user") + "'");
        } else {
            content.setStringValue("Hello to annonymous.");
        }
    }
}
```

## Define the tests

Now it is possible to define some tests. A test for a successful login and a
test for a not successful login shall be enough for the moment.

```java
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
        // and that the content field contains the correct text.
        new UpeAssertions(testUProcessEngine.getActiveProcess())
        .assertHasValue("content", "Hello to 'johndoe'");
        }
```

Running the test will result in two errors. Let's change this.

## Greenifying the failed login test.

First, a message is needed to inform a user about the wrong credentials. As seen in step 2, this is just a new annotated string constant in the LoginProcess.

```java
    @UpeProcessMessage(value="LOGIN-0002", level=UProcessMessage.MESSAGE_LEVEL_ERROR)
    public static final String login0002 = "The user and password combination is unknown. Please try again.";
```

In action method now needs some logic to:
* take the values from the process
* retrieve an instance of a UserMgr interface
* call the `isLoginCorrect` method on the instance
* jump to the target process or queue a process message for the user

In UPE, this looks like this
```java
    /**
     * This shows how to implement an action in a convenient way as a method
     * on a process component.
     *
     * The Action can be called via its name, "actLogin"
     * @param args
     */
    @UpeProcessAction("actLogin")
    public void login(Map<String, Object> args) {
        //Take the values from the process
        String user = getProcessElement("user", UProcessTextField.class).getStringValue();
        String password = getProcessElement("password", UProcessTextField.class).getStringValue();
        // retrieve an instance of a UserMgr interface
        UserMgr userMgr = UProcessBackend.getInstance().provide(UserMgr.class);
        //call the `isLoginCorrect` method on the instance
        Boolean result = userMgr.isLoginCorrect(user, password);
        //Jump to the target process or queue the process message LOGIN-0002 for the user
        if( result ) {
            Map<String, Object> callArgs = new HashMap<>();
            callArgs.put("user", user);
            getProcess().getProcessEngine().jumpToProcess(HelloWorldProcess.NAME, callArgs);
        } else {
            getProcess().getProcessEngine().queueProcessMessage(UProcessMessageStorage.getInstance().getMessage("LOGIN-0002"));
        }
    }
```

With this implementation of login, the `testUnsuccessfulLogin` test is green. But the second test now fails with a message.

`value mismatch: Expected 'Hello to 'johndoe'', actual ''`

## Greenifying the successful login test

The successful login test fails because there is no logic in the HelloWorldProcess to take the username and set the content value. To achieve this, the `initialize` method will get some code:

```java
    /**
     * The callArgs-Map from the login action will be passed to this
     * initialize method and can be used here.
     * @param args
     */
    @Override
    public void initialize(Map<String, Object> args) {
        // Is there a "user" argument in the args map?
        if( args.get("user") != null ) {
            // if yes, set the content to say hello to the user
            content.setStringValue("Hello to '"+args.get("user")+"'");
        } else {
            // if not, set the content to say hello to an anonymous user.
            content.setStringValue("Hello to anonymous.");
        }
    }
```

Starting test tests, and the world is green again.

Puh! This was a huge step. But you managed it. Well done!

# Takeaways

In this step, you learned the following:

* Providing access to backend logic to a process via an interface and the
  UProcessBackend class.
* Jumping from one process to another and sending the target process some data
* Retrieving data from a process call and reacting to them in the initialize method
* Providing mocks of a backend service in a JUnit test

Jumping to another process is one of two ways to navigate the application.
The other one is a call. They differ in that a jump replaces the current
process while a call adds the new process on top of a stack. When the called
process is finished, the calling process gets active again. 

But later, more on that topic.
