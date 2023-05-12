package upe.process.impl;

import upe.process.UProcessComponent;
import upe.process.UProcessField;

import java.io.Serializable;

public class AbstractUProcessFieldImpl extends AbstractUProcessElementImpl
		implements UProcessField {

	private Serializable value = null;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AbstractUProcessFieldImpl(UProcessComponent parent, String name) {
		super(parent, name);
	}

	public String getValueForFrontend() {
		if( value == null ) {
			return null;
		} else {
			return value.toString();
		}
	}

	public Serializable getValue() {
		return value;
	}

	public void setValueFromFrontend(String displayValue ) {
		setValue( displayValue );
	}

	@Override
	public void setValue(Serializable newValue) {
		if( value==null && newValue == null) {
			return;
		}
		if(value == null) {
			value = newValue;
			setNeedsRendering( true );
			return;
		}
		setNeedsRendering(!value.equals(newValue));
		value = newValue;
	}

}
