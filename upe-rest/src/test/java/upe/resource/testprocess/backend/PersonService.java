package upe.resource.testprocess.backend;

import upe.annotations.UpeBackendFacade;
import upe.resource.testprocess.dto.PersonDTO;

@UpeBackendFacade("PersonService")
public interface PersonService {
    PersonDTO loadByID( Object id);
    void store(PersonDTO person);
}
