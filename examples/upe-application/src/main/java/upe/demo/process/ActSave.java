package upe.demo.process;

import upe.backend.UProcessBackend;
import upe.demo.process.backend.PersonMgr;
import upe.demo.process.dto.Address;
import upe.demo.process.dto.Person;
import upe.process.UProcessComponent;
import upe.process.messages.UProcessMessage;
import upe.process.messages.UProcessMessageStorage;

import java.io.Serializable;
import java.util.Map;


public class ActSave extends ActSaveBase {

    public ActSave(UProcessComponent parent, String name) {
        super(parent, name);
    }

    public Serializable internalExecute( Map<String, Serializable> args ) {
        if( getProcess().getMaximumMessageLevel()>= UProcessMessage.MESSAGE_LEVEL_ERROR) {
            getProcess().getProcessEngine().queueProcessMessage(
                    UProcessMessageStorage.getInstance().getMessage("PE-002")
            );
            return null;
        }
        Person prs = new Person();
        prs.setAddress(new Address());
        PersonEditor personProcess = ((PersonEditor)this.getProcess().getProcessElement("person"));
        personProcess.mapToScaffolded(Person.class, prs);

        PersonMgr prsMgr = UProcessBackend.getInstance().provide(PersonMgr.class);
        Person storedPerson = prsMgr.savePerson(prs);

        return storedPerson;
    }
}
