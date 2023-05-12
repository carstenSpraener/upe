package upe.process;

import java.io.Serializable;
import java.util.Map;

public interface UProcessAction extends UProcessElement {

	public Serializable execute( Map<String, Serializable> args );

	public boolean isEnabled();
	
	public void setEnabled( boolean value );
}
