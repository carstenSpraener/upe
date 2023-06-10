# Testdriven Development of a Login Process with UPE

## What you will learn in this step

* Adding predefined validators in the constructor
* Adding costume messages to the application-wide message storage
* implementing a method validation on process fields

## Adding predefined validators

### Turning on the validation tests

It must be activated first to verify that the test will turn green.
The @Disable-Annotation moves from the class level to the
method `testLoginProcessActLoginDisabledUntillFilled()`.

That test will be turned green in sample-step3, but for now, it must be
disabled.

### Greenifying the validation tests

The first test case, `testLoginProcessNeedsUserAndPassword` can easily
be turned to green by adding validations to the login process.

```java
    public LoginProcess(UProcessEngine pe, String name) {
        super(pe, name);
        addValidator(new MandantoryValidator("user"));
        addValidator(new MandantoryValidator("password"));
    }
```

As shown above, the mandatory validation is part of the upe-core and can be added to the process inside the constructor. The constructor of
a process provides the process structure. The data initialization has
to be done in the `initialize()` method.

Rerunning the tests and the first test turns green.

```log
LoginProcessTest > testLoginProcessActLoginReportsPasswordToSmall() FAILED
    org.opentest4j.AssertionFailedError at LoginProcessTest.java:100
3 tests completed, 1 failed, 1 skipped
```

## Greenifying the "PasswordToSmall()" test

For the second validation, there is no standard validator. So it needs
to be implemented. Therefore two things are necessary. First, a
message has to be defined.

### Define a process message
A process message in UPE is a text with a unique identifier and a
severity level. All messages are members of the UProcessMessageStorage.

To define such a message, a process can define a `public static final String`
field with the `@UpeProcessMessage` annotation.

In the login process it is done as follows:

```java 
    @UpeProcessMessage(value="LOGIN-0001", level=UProcessMessage.MESSAGE_LEVEL_ERROR)
    public static final String login0001 = "The password needs to be at least 3 characters long.";
```

### Implement a validator method
The validation itself is implemented in a method of the LoginProcess. The
method is annotated with a `@UpeValidator` annotation. This annotation links the method with the process message defined in the previous step.

The parameters of the method are linked to process fields with a `@UpeProcessValue`
annotation. The parameter type must always be String because the
validator will receive the "valueForFrontend" from the process field.

The return value of the method is a boolean value that indicates whether
the error has to be raised.

The complete validation method looks like this:

```java
    @UpeValidator("LOGIN-0001")
    public boolean valPasswordToShort(@UpeProcessValue("password") String pwd) {
        return pwd==null ||pwd.length()<3;
    }
```

## Running the tests again

Another test run will show the tests are green now:

```log
? gradle :sample-step2:test
Starting a Gradle Daemon (subsequent builds will be faster)
...
BUILD SUCCESSFUL in 10s
7 actionable tasks: 7 executed
```

Nice!

## Takeaways
* Messages are text with a unique ID and a message severity level. They are stored in application-wide storage and could be used in the whole application
* Predefined validators are classes that implement a UValidator-Interface
  and can be used whenever it is appropriate. Use Validator-Classes if you
  want a reusable validation logic.
* Method-Validations are implemented as a method in a process component with
  a `@UpeValidator` annotation. Parameters are bound to process values via the
  `@UpeProcessValue` annotation. The parameter type must be `String`.
