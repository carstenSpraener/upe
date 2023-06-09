# Sample-Application of a Login Process

The projects smaple-1 to sample-N in this directory show how to
implement a login process as a UPE application. Each subproject
can be compiled, but it is not necessary green.

See the READMEs in the subprojects for further details. Here is 
just a little overview.

## Sample-step1: Implementing a process
Defines a simple login process with a user, pwd1 and pwd2 field 
and a unimplemented login action.

It defines a set of JUnit tests to check several validations. These 
tests are red and to greenify them is the task of the next steps.

## Sample-step2: Implementing validators
This project adds some mandatory validations to the login process
in order to get the first tests green.

It also defines a validation on the content of pwd1 and pwd2 which 
must be equal.

## Sampel-step3: Adding a rule to a process
This projects shows howto implement a rule to enable the login
action when all data is filled in and no errors are in the login 
process. It then enables the login action.

This should greenify the next test.

## Sample-step4: Calling a Backend, jump to next process
This project calls some Backend in the actLogin action and
queues a error message for the user, if login does not succeed.

If login succeeds, it jumps to a HelloWorld process.

## Sample-step5: Calling a sub-process

This project adds a actRegistration action to the login process which
will call a simple registration process. If the process returns, 
the new registered user should be able to do a login.

It shows how a process can call another process, send data
to the sub process and receive data on return. The registration
sub process does not define its fields on its own. It scaffolds 
a user dto. This project shows how to scaffold dtos to processes
and map data when save/load from the backend.

It also shows a complete JUnit test for the hole application.

## Sample-step6: Bind it to some UI

The final step binds the application to a UI. 

