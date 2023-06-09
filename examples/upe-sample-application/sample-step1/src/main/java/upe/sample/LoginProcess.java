package upe.sample;

import upe.annotations.UpeProcess;
import upe.annotations.UpeProcessAction;
import upe.annotations.UpeProcessField;
import upe.process.UProcessEngine;
import upe.process.UProcessTextField;
import upe.process.impl.AbstractUProcessImpl;

import java.util.Map;

@UpeProcess("login")
public class LoginProcess extends AbstractUProcessImpl {
    @UpeProcessField
    private UProcessTextField user;
    @UpeProcessField
    private UProcessTextField pwd;

    public LoginProcess(UProcessEngine pe, String name) {
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

    @UpeProcessAction("actLogin")
    public void login(Map<String,Object> args) {

    }
}
