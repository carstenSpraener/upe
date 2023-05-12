package upe.demo.process;

import upe.annotations.UpeProcess;
import upe.annotations.UpeProcessComponent;
import upe.annotations.UpeProcessField;
import upe.process.UProcess;
import upe.process.UProcessEngine;
import upe.process.UProcessModification;
import upe.process.UProcessTextField;
import upe.process.impl.AbstractUProcessImpl;

import java.io.Serializable;
import java.util.Map;

@UpeProcess("Person")
public class PersonProcess extends AbstractUProcessImpl {
    @UpeProcessField("name")
    private UProcessTextField name;
    @UpeProcessComponent("address")
    private AdressEditor adressEditor;

    public PersonProcess(UProcessEngine pe, String name) {
        super(pe, name);
    }


    @Override
    public void initialize(Map<String, Serializable> args) {
        UProcess p = this.getProcess();
        try (var procMod = new UProcessModification(this)) {
            this.name.setStringValue(null);
            super.doValidation();
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
