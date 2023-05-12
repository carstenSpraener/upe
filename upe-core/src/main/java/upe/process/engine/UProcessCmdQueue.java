package upe.process.engine;

import upe.exception.UPERuntimeException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UProcessCmdQueue implements Runnable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean isProcessing = false;
	private List<UProcessCommand> cmdList = new ArrayList<>();
	
	public void setProcessing() {
		isProcessing = true;
	}
	
	public boolean isProcessing() {
		return isProcessing;
	}

	public void appendCmd(UProcessCommand processCmd) {
		cmdList.add(processCmd);
	}

	public void run() {
		while( !cmdList.isEmpty() ) {
			UProcessCommand pCmd = cmdList.get(0);
			cmdList.remove(0);
			try {
				pCmd.execute(this);
				isProcessing = false;
			} catch( Exception e ) {
				throw new UPERuntimeException(e);
			}
		}
	}
}
