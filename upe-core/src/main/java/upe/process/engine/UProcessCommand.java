package upe.process.engine;

import java.io.Serializable;


public interface UProcessCommand extends Serializable {
	void execute( UProcessCmdQueue queue );
}
