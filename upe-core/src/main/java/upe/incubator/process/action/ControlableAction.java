package upe.incubator.process.action;

import upe.process.UProcess;

import java.io.Serializable;

public interface ControlableAction extends Serializable{
	void initialize( UProcess p );
	Serializable terminate();
}
