package upe.process.engine;

import upe.process.UProcess;

public class Jump2UProcessCmd extends AbstractUUProcessCmd {

	@Override
	public void internalExecute() {
		ActiveUProcessInfo oldProcessCall = processEngine.peekProcess();
		UProcess newUProcess = createProcess();
		newUProcess.inputStarts();
		newUProcess.initialize(getProcessArgs());
		newUProcess.inputStops();
		processEngine.finishProcess();
		processEngine.pushProcess(newUProcess, oldProcessCall.getReturnAction(), oldProcessCall.getCallingProcess());
	}
}
