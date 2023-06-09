package upe.process.engine;

import upe.exception.UPERuntimeException;
import upe.process.ApplicationConfiguration;
import upe.process.UProcess;
import upe.process.UProcessAction;
import upe.process.UProcessEngine;


import java.lang.reflect.Constructor;
import java.util.Map;

public abstract class AbstractUUProcessCmd implements UProcessCommand {

	protected UProcess callingUProcess = null;
	private Map<String, Object> processArgs;
	protected BaseUProcessEngine processEngine = null;
	protected String processName;
	protected UProcessAction returnAction;

	@Override
	public void execute(UProcessCmdQueue queue) {
		if( queue.isProcessing() ) {
			queue.appendCmd( this );
		} else {
			queue.setProcessing();
			internalExecute();
		}
	}

	public Map<String, Object> getProcessArgs() {
		return processArgs;
	}

	protected abstract void internalExecute();
	
	public void setCallingProcess(UProcess callingUProcess) {
		this.callingUProcess = callingUProcess;
	}

	public void setProcessArgs(Map<String, Object> processArgs) {
		this.processArgs = processArgs;
	}

	public void setProcessEngine(BaseUProcessEngine processEngine) {
		this.processEngine = processEngine;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public void setReturnAction(UProcessAction returnAction) {
		this.returnAction = returnAction;
	}

	protected UProcess createProcess() {
		return processEngine.getProcessInstance(processName);
	}

}
