package upe.process.impl;

import upe.process.UProcessComponent;
import upe.process.UProcessTextField;

public class UProcessTextFieldImpl extends AbstractUProcessFieldImpl implements
		UProcessTextField {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	boolean isEnabled = true;

	public UProcessTextFieldImpl(UProcessComponent parent, String name) {
		super(parent, name);
	}

	@Override
	public String getStringValue() {
		return (String)getValue();
	}

	@Override
	public void setStringValue(String value) {
		setValue(value);
	}

	@Override
	public void setEnabled( boolean value ) {
		isEnabled = value;
	}

	@Override
	public boolean isEnabled() {
		return isEnabled;
	}
}
