package upe.process;



public interface UProcessField extends UProcessElement {
	void setValue( Object value );
	Object getValue();
	
	String getValueForFrontend();
	
	void setValueFromFrontend( String displayValue );
}
