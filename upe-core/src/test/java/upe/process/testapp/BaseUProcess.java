package upe.process.testapp;

import upe.annotations.UpeProcess;
import upe.annotations.UpeProcessField;
import upe.process.UProcessEngine;
import upe.process.UProcessField;
import upe.process.UProcessTextField;
import upe.process.impl.AbstractUProcessImpl;


import java.util.Map;

@UpeProcess("TestProcess")
public class BaseUProcess extends AbstractUProcessImpl {
    @UpeProcessField
    private UProcessTextField name;

    public BaseUProcess(UProcessEngine pe, String name) {
        super(pe, name);
    }

    @Override
    public void initialize(Map<String, Object> args) {
        this.name.setValueFromFrontend("Base-Hallo");
        ((UProcessField)getProcessElement("/name")).setValueFromFrontend("Hallo");
    }

    public String getNameValueDirect() {
        return this.name.getValueForFrontend();
    }

    @Override
    public Map<String, Object> finish() {
        return null;
    }

    @Override
    public Map<String, Object> cancel() {
        return null;
    }
}
