package upe.process.choices;

import java.io.Serializable;

public class ChoiceableValueImpl implements ChoiceableValue {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Serializable key;
	private String[] displayValues;
	
	public ChoiceableValueImpl(Serializable key, String[] displayValues) {
		this.key = key;
		this.displayValues = displayValues;
	}

	@Override
	public String[] getDisplayValues() {
		return displayValues;
	}

	@Override
	public Serializable getKeyValue() {
		return key;
	}

}
