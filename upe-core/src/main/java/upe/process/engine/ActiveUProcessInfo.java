package upe.process.engine;

import upe.process.UProcessAction;



public class ActiveUProcessInfo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private UProcessAction returnAction;
	private upe.process.UProcess uProcess;
	private upe.process.UProcess callingUProcess;
	
	public ActiveUProcessInfo(upe.process.UProcess p, UProcessAction returnAction, upe.process.UProcess callingUProcess) {
		setProcess(p);
		setReturnAction(returnAction);
		setCallingProcess(callingUProcess);
	}
	
	public UProcessAction getReturnAction() {
		return returnAction;
	}
	public void setReturnAction(UProcessAction returnAction) {
		this.returnAction = returnAction;
	}
	public upe.process.UProcess getProcess() {
		return uProcess;
	}
	public void setProcess(upe.process.UProcess uProcess) {
		this.uProcess = uProcess;
	}
	public upe.process.UProcess getCallingProcess() {
		return callingUProcess;
	}
	public void setCallingProcess(upe.process.UProcess callingUProcess) {
		this.callingUProcess = callingUProcess;
	}
}
