package upe.process.testapp;

import upe.annotations.UpeProcess;
import upe.annotations.UpeProcessAction;
import upe.annotations.UpeProcessComponent;
import upe.annotations.UpeProcessField;
import upe.process.UProcessAction;
import upe.process.UProcessComponentList;
import upe.process.UProcessEngine;
import upe.process.UProcessTextField;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@UpeProcess("TestProcess")
public class DerivedUProcess extends BaseUProcess {
    @UpeProcessField("name")
    private UProcessTextField myName;

    @UpeProcessField("result")
    private UProcessTextField resultFromSubProcess;

    @UpeProcessComponent(value = "personList", listType=PersonEditor.class)
    private UProcessComponentList<PersonEditor> personList;

    public DerivedUProcess(UProcessEngine pe, String name) {
        super(pe, name);
    }

    @Override
    public void initialize(Map<String, Serializable> args) {
        super.initialize(args);
        this.personList.add(this.personList.createNewInstance());
    }

    @UpeProcessAction("actCallSubprocess")
    public Serializable callSubProcess(Map<String, Serializable> args) {
        UProcessAction retAct = (UProcessAction)getProcessElement("/actReturnFromCubProcess");
        getProcessEngine().callProcess("SubProcess", args, retAct);
        return null;
    }

    @UpeProcessAction("actReturnFromCubProcess")
    public Serializable returnFromSubProcess(Map<String, Serializable> args) {
        this.resultFromSubProcess.setValue(args.get("result"));
        return null;
    }
}
