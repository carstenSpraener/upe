# Testdriven Development of a Login Process with UPE

## What you will learn in this step

* What is a rule in UPE
* Checking the maximum severity level of a process component
* Adding a rule to enable and disable an action

## Adding a rule to a process
A rule is some logic executed whenever requested process fields
change. A rule is a pendant to a change listener in classic UI
programming.

### Turning on the testLoginProcessActLoginDisabledUntilFilled

First, remove the `@Disabled` annotation from the test method and start
it to see that it is red.

```java
    @Test
    void testLoginProcessActLoginDisabledUntilFilled() throws Exception {
```

A test run results in
```log
Expected process element /actLogin to be enabled, but it is disabled.
upe.test.UPEAssertionException: Expected process element /actLogin to be enabled, but it is disabled.
```

### Defining a method-based rule on the input fields

OK. Now let's implement a rule bound to the process fields
and change the state of the login action when there are more
errors on the user and password fields. Here is how this is done:

```java
    @UpeRule
    public void rulEnableActLogin(@UpeProcessValue("user") UProcessField user, @UpeProcessValue("password") UProcessField password) {
        UProcessAction actLogin = getProcessElement("actLogin", UProcessAction.class);
        boolean isEnabled = user.getMaximumMessageLevel() <= UProcessMessage.MESSAGE_LEVEL_WARNING &&
                            password.getMaximumMessageLevel() <= UProcessMessage.MESSAGE_LEVEL_WARNING;
        actLogin.setEnabled(isEnabled);
    }
```
The rule is not interested in the values of the process fields but in the
error state of the process fields. Therefore, the parameters are of type
UProcessElement. It is the most abstract interface to get the severity
level of a ProcessElement.

If both process elements have a maximum message severity of WARNING or less, the
action login is enabled. Otherwise, it is disabled.

Let's write a test to see that the action is disabled again whenever one
the field gets an error again.

This test is more extended but relatively easy. It just fills and clears
the user and password fields and checks the state of the login action:

```java

    /**
     * the action login should be disabled whenever the user and password
     * have errors and enabled if not.
     *
     * @throws Exception
     */
    @Test
    void testLoginProcessActLoginDisabledAgain() throws Exception {
        uut.initialize(new HashMap<>());
        UpeAssertions upeAssertions = new UpeAssertions(uut);

        try (UProcessModification mod = new UProcessModification(uut)) {
            uut.getProcessElement("/user", UProcessTextField.class).setStringValue("johndoe");
            uut.getProcessElement("/password", UProcessTextField.class).setStringValue("password");
        }
        upeAssertions.assertEnabled("/actLogin");
        try (UProcessModification mod = new UProcessModification(uut)) {
            uut.getProcessElement("/user", UProcessTextField.class).setStringValue("johndoe");
            uut.getProcessElement("/password", UProcessTextField.class).setStringValue("");
        }
        upeAssertions.assertDisabled("/actLogin");
        try (UProcessModification mod = new UProcessModification(uut)) {
            uut.getProcessElement("/user", UProcessTextField.class).setStringValue("johndoe");
            uut.getProcessElement("/password", UProcessTextField.class).setStringValue("p");
        }
        upeAssertions.assertDisabled("/actLogin");
        try (UProcessModification mod = new UProcessModification(uut)) {
            uut.getProcessElement("/user", UProcessTextField.class).setStringValue("johndoe");
            uut.getProcessElement("/password", UProcessTextField.class).setStringValue("password");
        }
        upeAssertions.assertEnabled("/actLogin");
        try (UProcessModification mod = new UProcessModification(uut)) {
            uut.getProcessElement("/user", UProcessTextField.class).setStringValue("");
            uut.getProcessElement("/password", UProcessTextField.class).setStringValue("password");
        }
        upeAssertions.assertDisabled("/actLogin");

    }
```

## Takeaways

* Rules are the UPE pendant to onChange listeners in classic UI.
* They can be defined by a method annotated with the `@UpeRule` annotation
* The parameters can take the ProcessElement or the value of the Process Element (or even both in separate parameters)
* Rules are triggered whenever the value or the message state of one of its
  fields changes.
* It is possible, but quite unusual, to define a rule in a separate class.
  Rules are tightly bound to the process elements, so they are mostly only
  useful inside the process.
