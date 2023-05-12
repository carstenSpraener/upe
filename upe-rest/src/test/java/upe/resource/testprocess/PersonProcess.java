package upe.resource.testprocess;

import upe.annotations.UpeProcess;
import upe.process.UProcessElementFactory;
import upe.process.UProcessElementSystem;
import upe.process.UProcessEngine;
import upe.process.UProcessTextField;
import upe.process.impl.AbstractUProcessImpl;
import upe.process.impl.UProcessTextFieldImpl;
import upe.process.validation.UProcessValidator;

import java.io.Serializable;
import java.util.Map;

@UpeProcess("Person")
public class PersonProcess extends AbstractUProcessImpl {
    private UProcessTextField name;
    private AdressEditor adressEditor;

    public PersonProcess(UProcessEngine pe, String name) {
        super(pe, name);
        UProcessElementFactory ef =  UProcessElementSystem.getProcessElementFactory();
        this.name = ef.newTextField(this, "name");
        this.adressEditor = new AdressEditor(this, "adress");
        this.addValidator(new NameValidator());
    }


    @Override
    public void initialize(Map<String, Serializable> args) {
        inputStarts();
        this.name.setStringValue(null);
        super.doValidation();
        inputStops();
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
