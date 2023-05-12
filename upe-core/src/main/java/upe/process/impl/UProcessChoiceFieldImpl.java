package upe.process.impl;

import upe.process.UProcessChoiceField;
import upe.process.UProcessComponent;
import upe.process.choices.ChoiceSet;

public class UProcessChoiceFieldImpl extends AbstractUProcessFieldImpl implements
		UProcessChoiceField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ChoiceSet myChoiceSet = null;
	
	public UProcessChoiceFieldImpl(UProcessComponent parent, String name) {
		super(parent, name);
	}

	@Override
	public ChoiceSet getChoiceSet() {
		return myChoiceSet;
	}

	@Override
	public void setChoiceSet(ChoiceSet data) {
		myChoiceSet = data;
		super.setValue(null);
		setNeedsRendering(true);
	}

}
