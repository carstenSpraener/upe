package upe.resource;

import upe.process.UProcess;
import upe.process.UProcessAction;
import upe.process.engine.AbstractUUProcessCmd;
import upe.process.engine.BaseUProcessEngine;
import upe.process.engine.CallUProcessCmd;

import java.io.Serializable;
import java.util.Map;

public class UpeDialogProcessEngine extends BaseUProcessEngine {

    public void callProcessForRestore(String processName, Map<String, Serializable> processArgs, UProcessAction returnAction) {
        AbstractUUProcessCmd cmd = new CallUProcessCmd() {
            @Override
            public void internalExecute() {
                UProcess p = createProcess();
                ((UpeDialogProcessEngine)processEngine).pushProcess(p, returnAction, callingUProcess);
            }
        };

        setupProcessCmd(cmd, processName, processArgs, returnAction);
        queueCommand(cmd);
    }
}
