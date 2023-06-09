package upe.process.choices;



public class ChoiceableValueImpl implements ChoiceableValue {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Object key;
	private String[] displayValues;
	
	public ChoiceableValueImpl(Object key, String[] displayValues) {
		this.key = key;
		this.displayValues = displayValues;
	}

	@Override
	public String[] getDisplayValues() {
		return displayValues;
	}

	@Override
	public Object getKeyValue() {
		return key;
	}

}
