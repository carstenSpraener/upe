package upe.process;


import java.util.Map;

public interface UProcessAction extends UProcessElement {

	public Object execute( Map<String, Object> args );

	public boolean isEnabled();
	
	public void setEnabled( boolean value );
}
