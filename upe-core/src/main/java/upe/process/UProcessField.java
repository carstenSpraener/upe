package upe.process;

import java.io.Serializable;

public interface UProcessField extends UProcessElement {
	void setValue( Serializable value );
	Serializable getValue();
	
	String getValueForFrontend();
	
	void setValueFromFrontend( String displayValue );
}
