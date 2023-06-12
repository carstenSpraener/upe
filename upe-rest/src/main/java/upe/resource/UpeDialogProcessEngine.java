package upe.resource;

import upe.process.UProcess;
import upe.process.UProcessAction;
import upe.process.engine.AbstractUUProcessCmd;
import upe.process.engine.ActiveUProcessInfo;
import upe.process.engine.BaseUProcessEngine;
import upe.process.engine.CallUProcessCmd;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UpeDialogProcessEngine extends BaseUProcessEngine {
    private UProcessChangeListener changeListener = null;

    public void setChangeListener(UProcessChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public void callProcessForRestore(String processName, Map<String, Object> processArgs, UProcessAction returnAction) {
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


    public void finishProcessForRestore() {
        super.popProcess();
    }

    @Override
    public void callProcess(String processName, Map<String, Object> processArgs, UProcessAction returnAction) {
        if( this.changeListener!=null ) {
            if( hasActiveProcess() ) {
                this.changeListener.callProcess(getActiveProcess().getName(), processName, processArgs, returnAction.getElementPath());
            } else {
                this.changeListener.callProcess(null, processName, processArgs, null);
            }
        }
        super.callProcess(processName, processArgs, returnAction);
    }

    @Override
    public void jumpToProcess(String processName, Map<String, Object> processArgs) {
        if( this.changeListener!=null ) {
            this.changeListener.jumpToProcess(getActiveProcess().getName(), processName, processArgs);
        }
        super.jumpToProcess(processName, processArgs);
    }

    @Override
    public Map<String, Object> cancelProcess() {
        String canceledProcessName = getActiveProcess().getName();
        Map<String, Object> resultArgs = super.cancelProcess();
        if( this.changeListener!=null ) {
            this.changeListener.cancelProcess(canceledProcessName, resultArgs);
        }
        return resultArgs;
    }

    @Override
    public Map<String, Object> finishProcess() {
        ActiveUProcessInfo pc = peekProcess();
        String finishedProcess = getActiveProcess().getName();
        Map<String,Object> result = pc.getProcess().finish();
        if( result==null ) {
            result = new HashMap<>();
        }
        result.put( PROCESS_RESULT_STATE, PROCESS_FINISH );
        // Inform a delta recorder about the neew actice process so that
        // it can start recording before return action is executed.
        if( this.changeListener!=null ) {
            this.changeListener.finishProcess(finishedProcess, result);
        }
        popProcess();
        if( pc.getReturnAction() != null ) {
            pc.getReturnAction().execute(result);
        }
        return result;
    }

}
