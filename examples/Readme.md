# WARNING! Highly under construction

__The projects in this directory are highly under
construction. The purpose of the projects is
to test the implementation of upe-core, upe-rest,
upe-angular-frontend and the cgV19-Cartridge for
upe.__

Nevertheless, you can look around and understand how things are meant to be used. However, please do not blame me for the code in this directory.

## The projects are:

### upe-application-cartridge
A [cgV19-cartridge](https://github.com/carstenSpraener/cgV19) for model driven development with
upe.

### upe-application
A model-driven developed application to demonstrate
the use of upe. The model is a [VisualParadigm](https://www.visual-paradigm.com/download/community.jsp)
UML-Model.

The application runs as a Spring Boot application
without a front end.

### upe-ngclient
A prototype/POC for an Angular frontend for an
upe application. Not very nice but with some
exciting features.

![img_2.png](images/img_2.png)

## Features

* All business logic is running on the server. The
  front end has just code to split the fields into
  views and bind some input fields and buttons to
  process elements. Frontend-Code is mainly like this:

```hmtl
<div [upeProcess]="'personProcess'" style="margin-bottom: 60px">
<div [upeView]="'init'" class="upePanel">
    <h3>InitView</h3>

    <label>Name:</label>
    <input [upeField]="'/person/name'"/>
    <button [upeViewNav]="'address'">Address</button>
 </div>

```

* validation errors will be displayed immediately. Also, enable/disable elements or
  visible/hide process elements.

* Forward and Backword-Button in the browser are
  allowed. You can go back through your inputs, and
  errors will reappear.

* The link in the browser can be shared. The
  link will open the application in precisely the same state
  you see.
* There is no "Session" as in classic web applications. Your
  running application is a REST-Resource where you can GET, PUT and POST
  some Data.
* Synchronisation between the front end and the server-side upe application is
  done via PUT-Requests and ProcessDelta-Response.


## Model Driven Development with [cgV19](https://github.com/carstenSpraener/cgV19)

As mentioned above, the application is a model-driven developed application. This means you will have a model that feeds a generator that generates the majority of the code of your project.

__Process Layer of the demo application__

![upe-demo-process-layer-model.png](images/upe-demo-process-layer-model.png)

__DTO-Layer of the demo application__

![upe-application-dto-layer.png](images/upe-application-dto-layer.png)

__Backend-Layer of the demo application__

![ue-demo-backed-layer.png](images/ue-demo-backed-layer.png)
