package upe.process.testapp;

import upe.annotations.UpeProcess;
import upe.annotations.UpeProcessField;
import upe.process.UProcessEngine;
import upe.process.impl.AbstractUProcessImpl;


import java.util.Map;

@UpeProcess("SecondProcess")
public class SecondUProcess extends AbstractUProcessImpl {

    @UpeProcessField()
    private PersonEditor secondPersonEditor;

    public SecondUProcess(UProcessEngine pe, String name) {
        super(pe, name);
    }


    @Override
    public void initialize(Map<String, Object> args) {
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
