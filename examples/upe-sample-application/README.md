# Sample-Application of a Login Process

The projects sample-step1 to sample-step7 in this directory show how to
implement a login process as a UPE application. Each subproject
can be compiled, but it is not necessarily green.

See the READMEs in the subprojects for further details.
Here is an overview.

## [Sample-step1](sample-step1/README.md): Implementing a process
Defines a simple login process with a user and password field
and an unimplemented login action.

It defines a set of JUnit tests to check several validations. These
tests are red, and to greenifying them is the task of the next steps.

## [Sample-step2](sample-step2/README.md): Implementing validators
This project adds mandatory validations to the login process
to get the first tests green.

## [Sample-step3](sample-step3/README.md): Adding a rule to a process
This project shows how to implement a rule to enable the login
action when all data is filled in and no errors are in the login
process. It then allows the login action.

This should turn another test to green.

## [Sample-step4](sample-step4%2FREADME.md): Calling a Backend, jump to another process
This project calls some Backend in the actLogin action and
queues an error message for the user if login does not succeed.

If the login succeeds, it jumps to a HelloWorld process showing the username.

## [Sample-step5](sample-step5%2FREADME.md): Calling a sub-process

This project adds an actRegistration action to the login process, which
will call a simple registration process. If the process returns,
the newly registered user should be able to do a login.

It shows how a process can call another process, send data
to the subprocess and receive data on return. The registration
subprocess does not define its fields on its own. It scaffolds
a user-DTO. This project shows how to scaffold a DTO to a process
and map data when saving or loading from the backend.

It also shows a complete JUnit test for the whole application.

## Sample-step6: Bind it to some UI

The final step binds the application to a UI. 
