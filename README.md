# upe - Universal Process Engine
or __U__ I, __P__ rocess and __E__ verything else

UPE is a framework that allows to implement the most parts of an 
application in a portable, scalable and testable way. The intention is
to make your expensive business code free from UI technologies, backend
technologies or other rapidly changing frameworks. This is achived by:

* Providing a framework that has a very flat dependency graph
* Providing structures to build reusable components
* A programming model independent of the runtime environment
* As a side product the application is highly testable

__At the moment there is no Getting Started.__ 
## [Take a look at the examples to get an idea](examples/Readme.md)

You can build __highly interactive complex applications__ where every field on
a dialog is a resource. In a browser the Backward-Button is not forbidden,
and you can even send the link to your specific dialog to a colleague to
let him do further work. No browser session required.

## The concept of UPE

UPE has its roots in the 2000er years. The concept has proven to be
highly reliable and extensible. Applications developed with this concept are running
for 20 years now with changing environments like:

* Fat client in a old school client server environment
* J2EE as a full stacked web application
* Thin client / Tomcat backend in a distributed environment with central data storage
* Sring Boot / Angular with highly interactive form fields and shareable links


### UProcess and UProcessEngine

A UPE application is a collection of UProcess-Classes. Each UProcess has a unique
name. The UProcesses are managed by a UProcessEngine.

There are several ProcessEngines to run such applications. UPE supports at the moment 
swing/javaFX, Tomcat/JSP, SpringBoot/Angular, and Test

A Process can call a sub process of jump to another process. Several life cycle methods inform
the application of important life cycle events like started, finished or canceled. 

#### Composite-Pattern for UProcess

A UProcess can have several UProcessComponents. Each UProcessComponent can
hold other UProcessComponents. 

UProcessComponents are intended to be reused by many UProcesses/UProcessComponents.
UProcessComponents building a tree of UProcessComponents.

At the leaves of this UProcessComponents are UProcessElements which do not hold
further children. The  children are UProcessFields and UProcessActions. These
fields are intended to be rendered by some UI but do not have a specific UI.

#### UProcessFields
UProcessFields can be set visible, enabled, holding a value and several messages
for the user. They can be addressed by a simple path string. Validators of a 
UProcess will check the state and add/remove Messages to a UProcessElement.

#### UProcessAction
A UProcesAction typically reacts on a Button press from the frontend and 
will provide the required business logic. It collects data, send them to
some backend, receives the answer and maps the result into the process.

It als calls/jumps other processes. 

### Frontends

A frontend for UPE is ideally free of business logic. Its only  purpose is to
group process fields into panels and navigate between panels on the same 
process.

It communicates with the process over a string based interface in the form

```java
setField("/person/address/street","5th Avnue")
getFieldState("/person/address/street")
triggerAction("/actStore")
```

### Backends

Backends are accessed with a command pattern, where each command is a 
unit of work. The mechanism of executing a command depends on the distribution
and environment the application is running in.

### Testing

A UPE application can be tested with a special "test" frontend. As described
in the frontend section the communication between frontend and UPE is simple by
strings/text. This makes the testing very easy, and you can write test scripts
that drive a whole application, checking the messages and states of the process
fields and running through a whole process.

