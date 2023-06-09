package upe.process.impl;

import upe.process.UProcessComponent;
import upe.process.UProcessTextField;

public class UProcessTextFieldImpl extends AbstractUProcessFieldImpl implements
		UProcessTextField {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
}
