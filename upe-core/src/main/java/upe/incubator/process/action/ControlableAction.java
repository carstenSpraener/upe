package upe.incubator.process.action;

import upe.process.UProcess;



public interface ControlableAction {
	void initialize( UProcess p );
	Object terminate();
}
