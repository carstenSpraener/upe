package upe.test;

import upe.annotations.UpeProcess;
import upe.annotations.UpeProcessField;
import upe.backend.UProcessBackend;
import upe.process.UProcessEngine;
import upe.process.UProcessTextField;
import upe.process.impl.AbstractUProcessImpl;
import upe.process.validation.impl.MandatoryValidator;

import java.util.Map;

@UpeProcess("ASimpleProcess")
public class ASimpleProcess extends AbstractUProcessImpl {
    public static final String NAME = "ASimpleProcess";

    @UpeProcessField
    private UProcessTextField name;

    public ASimpleProcess(UProcessEngine pe, String name) {
        super(pe, name);
        addValidator(new MandatoryValidator("name"));
    }

    @Override
    public void initialize(Map<String, Object> args) {
        String value = UProcessBackend.getInstance().provide(BackendService.class).get();
        this.getProcessElement("name", UProcessTextField.class).setStringValue(value);
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

