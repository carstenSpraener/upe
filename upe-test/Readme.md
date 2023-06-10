# Testing an UPE Application

UPE is by the core extremly easy to test. This is because the architecture
does not require any specific environment but just a UProcess Instance.

With this UProcess instance a frontend communicates via string paths and
string values. One of this possible Frontends could be a JUnit test.

The Backend is provided via an interface, that's implementation is retrieved
via the UProcessBackend. This interfaces can easily be mocked.

So you can test the whole UProcess with all its validations, rules and
actions without any other infrastructure. All you need is a UProcessEngine
and a Running instance of your process. 

You can do this by hand or use the provided JUpiter extension.

## Using the JUpiter extension

### Starting the process engine

For JUpiter you need to use junit-5 of course. With that setup you can 
write your test as follows:

```java
// A UPE-Test for a given UPE PersonProcess
@ExtendWith(
        UpeTestExtension.class
)
@UpeApplication(
        TestProcess.class
)
class TestUProcessEngineTest {
```
}

This test will get a running instance of an UpeTestProcessEninge for each 
test inside the class. 

### Access to a Process under Test
To access the instance you can use:

```java
    @UpeProcessToTest(ASimpleProcess.NAME)
    private ASimpleProcess put;
```

This will a created instance of your UProcess and inject it into the
field `put`.

### Mocking Backend-Service

The UProcess will communicate with some Backend via an ordinary interface.
Each Service needs to have a registered provider in the UProcessBackend. In
the test environment these interfaces need to get a mock.

So the first step is to build a mocked instance of a backend service.

```java
    private BackendService stringProvider = mock(BackendService.class);

    @BeforeEach
    public void setup() {
        when(stringProvider.get()).thenReturn("Hello World");
    }
```
In this example, the stringProvider will simply return the text `"Hello World"`.
In order to bind this (very simple) service to the UProcessBackend, you just
annotate it with an `@UpeBackendComponent` annotation like this:
```java
    @UpeBackendComponent(BackendService.NAME)
    private BackendService stringProvider = mock(BackendService.class);
```

Now, whenever the process will request a implementatino of BackendService,
the Backend will provide your mock.

## Assertions on process level

This was all just the setup of a test. But what you really want to do, is
to check, that your process works as expected. Therefor you need to implement 
some tests that simulate some user input and check the process state.

### Enhance the _ASimpleProcess_

To demonstrate the test of process behaviour the process needs to get
some behaviour at all. So let's add a simple mandatory validation on the
name field. In the constructor of the ASimpleProcess let's add  

```java
        addValidator(new MandantoryValidator("name"));
```

Now there should raise an error (namely "UPE0001") whenever the name-Field
gets empty. Let's check! 

### UpeAssertions

Since the name-Field is set at the initialization, we need to 
modify it first, than check the error, modify it again by setting some value
and look if the error disapeared:
```java
    @Test
    void testNoNameRaisesError() throws Exception {
        try(UProcessModification pm = new UProcessModification(uut)) {
            uut.initialize(new HashMap<>());
            uut.setFieldValue("name", "");
        }
        new UpeAssertions(uut)
                .assertHasError("/name", MandantoryValidator.MSG_ID)
                .assertMaxMsgLevel("/name", UProcessMessage.MESSAGE_LEVEL_ERROR)
        ;
        try(UProcessModification pm = new UProcessModification(uut)) {
            uut.setFieldValue("name", "Moin!");
        }
        new UpeAssertions(uut)
                .assertNotHasError("/name",  MandantoryValidator.MSG_ID)
                .assertMaxMsgLevel("/name", UProcessMessage.MESSAGE_LEVEL_NONE)
                ;
    }
```



## The complete example

### The Test-Class

```Java
package upe.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import upe.annotations.UpeApplication;
import upe.process.UProcessEngine;
import upe.process.UProcessModification;
import upe.process.UProcessTextField;
import upe.process.messages.UProcessMessage;
import upe.process.validation.impl.MandatoryValidator;
import upe.test.annotations.UInject;
import upe.test.annotations.UpeBackendComponent;
import upe.test.annotations.UpeProcessToTest;
import upe.test.jupiter.UpeTestExtension;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(
        UpeTestExtension.class
)
@UpeApplication(
        ASimpleProcess.class
)
class TestUProcessEngineTest {

    @UInject
    private UProcessEngine processEngine;

    @UpeProcessToTest(ASimpleProcess.NAME)
    private ASimpleProcess uut;

    @UpeBackendComponent(BackendService.NAME)
    private BackendService stringProvider = mock(BackendService.class);

    @BeforeEach
    public void setup() {
        when(stringProvider.get()).thenReturn("Hello World");
    }

    @Test
    void testProcessSetup() {
        assertNotNull(uut);
        assertNotNull(processEngine);
        try(UProcessModification pm = new UProcessModification(uut)) {
            uut.initialize(new HashMap<>());
        }
        assertEquals("Hello World", uut.getProcessElement("/name", UProcessTextField.class).getStringValue());
    }

    @Test
    void testNoNameRaisesError() throws Exception {
        // Initialize the process and set the name value to empty string
        try(UProcessModification pm = new UProcessModification(uut)) {
            uut.initialize(new HashMap<>());
            uut.setFieldValue("name", "");
        }
        // check that the expected error occurred
        new UpeAssertions(uut)
                .assertHasError("/name", MandantoryValidator.MSG_ID)
                .assertMaxMsgLevel("/name", UProcessMessage.MESSAGE_LEVEL_ERROR)
        ;
        // modify the name field and set some value
        try(UProcessModification pm = new UProcessModification(uut)) {
            uut.setFieldValue("name", "Moin!");
        }
        // check that the expected error disappeared.
        new UpeAssertions(uut)
                .assertNotHasError("/name",  MandantoryValidator.MSG_ID)
                .assertMaxMsgLevel("/name", UProcessMessage.MESSAGE_LEVEL_NONE)
                ;
    }
}
```

### The Simple _ASimpleProcess_

```java
@UpeProcess("ASimpleProcess")
public class ASimpleProcess extends AbstractUProcessImpl {
    public static final String NAME = "ASimpleProcess";

    @UpeProcessField
    private UProcessTextField name;

    public ASimpleProcess(UProcessEngine pe, String name) {
        super(pe, name);
        addValidator(new MandantoryValidator("name"));
    }

    @Override
    public void initialize(Map<String, Serializable> args) {
        String value = UProcessBackend.getInstance().provide(BackendService.class).get();
        this.getProcessElement("name", UProcessTextField.class).setStringValue(value);
    }

    @Override
    public Map<String, Serializable> finish() {
        return null;
    }

    @Override
    public Map<String, Serializable> cancel() {
        return null;
    }
}
```

### The __BackendService__

```java
@UpeBackendFacade("stringProvider")
public interface BackendService {
    String NAME = "stringProvider";

    String get();
}
```
