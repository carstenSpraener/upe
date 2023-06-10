# upe - Universal Process Engine
or __U__ I, __P__ rocess and __E__ verything else

UPE is a framework that allows to implement the most parts of an
application in a portable, scalable, and testable way. The intention is
to make the expensive business code free from UI technologies, backend
technologies, or other rapidly changing frameworks. This mission is achieved by:

* Providing a framework that has a very flat dependency graph
* Providing structures to build reusable components
* A single-threaded programming model independent of the runtime environment
* As a side product, the application is highly testable

## [Sample-Steps](examples/upe-sample-application/README.md): A tour through UPE

UPE can build __highly interactive complex applications__ where every field on
a dialog is a resource. In a browser, the Backward-Button is not forbidden,
and a user can even send the link to a specific dialog to a colleague to
let him do further work. No browser session is required.

## The concept of UPE

UPE has its roots in the 2000er years. The concept has proven to be
highly reliable and extensible. Applications developed with this concept have been running
for 20 years now with changing environments like:

* Fat client in an old-school client-server environment
* J2EE as a fully stacked web application
* Thin client / Tomcat backend in a distributed environment with central data storage
* Sring Boot / Angular with highly interactive form fields and shareable links


### UProcess and UProcessEngine

A UPE application is a collection of UProcess-Classes. Each UProcess has a unique
name. A UProcessEngine manages the UProcesses.

There are several UProcessEngines to run such applications. UPE supports at the moment
swing/JavaFX, Tomcat/JSP, SpringBoot/Angular, and Test

A Process can call a subprocess or jump to another process. Several life cycle methods inform about important life-cycle events like started, finished, or canceled.

#### Composite-Pattern for UProcess

A process can have several UProcessComponents. Each UProcessComponent can
hold other UProcessComponents.

UProcessComponents are intended to be reused by many UProcesses/UProcessComponents.
UProcessComponents building a tree of UProcessComponents.

At the leaves of this UProcessComponents are UProcessElements which do not hold
further children. The leaves are UProcessFields and UProcessActions. These
fields are intended to be rendered by some UI but do not have a specific UI.

#### UProcessFields
UProcessFields can be set visible, enabled, holding a value and several messages for the user. A simple path string can address them. Validators of a UProcess will check the state and add/remove Messages to a UProcessElement.

#### UProcessAction
A UProcessAction typically reacts on a Button press from the front end and
will provide the required business logic. It collects data, sends them to
some backend, receives the answer, and maps the result into the process.

It also calls/jumps to other processes.

### Frontends

A front end for UPE is ideally free of business logic. Its only  purpose is to
group process fields into panels and navigates between panels on the same
process.

It communicates with the process over a string-based interface in the form.

```java
setField("/person/address/street","5th Avenue")
getFieldState("/person/address/street")
triggerAction("/actStore")
```

### Backends

Backends are accessed with a facade pattern, where each method is a unit of work. The method executing mechanism depends on the distribution and environment in which the application is running in.

### Testing

A UPE application can be tested with a unique "test" frontend. As described in the front-end section, the communication between the front end and UPE is simple by strings/text. This approach makes the testing very easy, and a programmer can write test scripts that drive a whole application, checking the messages and states of the process
fields and running through a whole process.
