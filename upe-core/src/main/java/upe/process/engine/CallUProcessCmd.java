package upe.process.engine;


import upe.process.UProcess;

public class CallUProcessCmd extends AbstractUUProcessCmd {
	@Override
	public void internalExecute() {
		UProcess p = createProcess();
		processEngine.pushProcess(p, returnAction, callingUProcess);
		p.inputStarts();
		p.initialize(getProcessArgs());
		p.inputStops();
	}
}
