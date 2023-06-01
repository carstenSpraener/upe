package upe.resource.testprocess;

import upe.annotations.UpeProcess;
import upe.annotations.UpeProcessAction;
import upe.annotations.UpeProcessComponent;
import upe.annotations.UpeProcessField;
import upe.process.UProcessComponentList;
import upe.process.UProcessEngine;
import upe.process.UProcessModification;
import upe.process.UProcessTextField;
import upe.process.impl.AbstractUProcessImpl;
import upe.resource.testprocess.action.ActSelectedAdressOK;

import java.io.Serializable;
import java.util.Map;

@UpeProcess("Person")
public class PersonProcess extends AbstractUProcessImpl {
    @UpeProcessField("name")
    private UProcessTextField name;
    @UpeProcessComponent("adress")
    private AdressEditor adressEditor;
    @UpeProcessComponent(value="addressList", listType = AdressEditor.class)
    private UProcessComponentList<AdressEditor> adressList;

    @UpeProcessAction("actSelectedAdressOK")
    private ActSelectedAdressOK actSelectedAdressOK;

    public PersonProcess(UProcessEngine pe, String name) {
        super(pe, name);
        this.addValidator(new NameValidator());
    }


    @Override
    public void initialize(Map<String, Serializable> args) {
        try(UProcessModification mod = new UProcessModification(this)) {
            this.name.setStringValue(null);
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
}
