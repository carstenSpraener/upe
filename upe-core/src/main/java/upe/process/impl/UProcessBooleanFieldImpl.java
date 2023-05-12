package upe.process.impl;

import upe.converter.StringToBooleanConverter;
import upe.process.UProcessBooleanField;
import upe.process.UProcessComponent;

public class UProcessBooleanFieldImpl extends AbstractUProcessFieldImpl implements UProcessBooleanField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UProcessBooleanFieldImpl(UProcessComponent parent, String name) {
		super(parent, name);
	}

	@Override
	public void setBooleanValue( Boolean value ) {
		super.setValue(value);
	}

	@Override
	public Boolean getBooleanValue() {
		return (Boolean)getValue();
	}

	@Override
	public String getValueForFrontend() {
		Boolean value = getBooleanValue();
		if( value == null ) {
			return null;
		}
		return value.booleanValue() ? "true" : "false";
	}

	@Override
	public void setValueFromFrontend( String value )  {
		setValue( new StringToBooleanConverter().convert(value));
	}
}
