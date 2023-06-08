package upe.resource.testprocess;

import upe.annotations.UpeProcess;
import upe.annotations.UpeProcessAction;
import upe.annotations.UpeProcessComponent;
import upe.annotations.UpeScaffolds;
import upe.backend.UProcessBackend;
import upe.process.UProcessAction;
import upe.process.UProcessComponentList;
import upe.process.UProcessEngine;
import upe.process.UProcessModification;
import upe.process.impl.AbstractUProcessImpl;
import upe.resource.testprocess.action.ActSelectedAdressOK;
import upe.resource.testprocess.backend.PersonService;
import upe.resource.testprocess.dto.PersonDTO;

import java.io.Serializable;
import java.util.Map;

@UpeProcess("Person")
@UpeScaffolds(PersonDTO.class)
public class PersonProcess extends AbstractUProcessImpl {
    public static final String ARG_PERSON_ID = "ID";
    @UpeProcessComponent("selectedAddress")
    private AdressEditor adressEditor;

    @UpeProcessAction("actSelectedAdressOK")
    private ActSelectedAdressOK actSelectedAdressOK;

    public PersonProcess(UProcessEngine pe, String name) {
        super(pe, name);
        this.addValidator(new NameValidator());
    }


    @Override
    public void initialize(Map<String, Serializable> args) {
        try(UProcessModification mod = new UProcessModification(this)) {
            resetAllValues();
            if( args.get(ARG_PERSON_ID) != null ) {
                getProcessElement("actLoadPerson", UProcessAction.class).execute(args);
            }
        }
    }

    @Override
    public Map<String, Serializable> finish() {
        return null;
    }

    @Override
    public Map<String, Serializable> cancel() {
        return null;
    }

    @UpeProcessAction("actLoadPerson")
    public void loadPerson(Map<String, Serializable> args) {
        PersonService srv = UProcessBackend.getInstance().provide(PersonService.class);
        PersonDTO personFromBackend = srv.loadByID(args.get(ARG_PERSON_ID));
        mapFromScaffolded(personFromBackend);
    }
}
