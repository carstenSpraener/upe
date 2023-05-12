package upe.process.choices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements a ChoiceSet on a key value and an array of Strings.
 * 
 * @author casi
 *
 */
public class StringArrayChoiceSetImpl implements ChoiceSet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<ChoiceableValue> myValueList = new ArrayList<>();
	
	@Override
	public List<ChoiceableValue> getChoiceableValues() {
		return myValueList;
	}

	public void addChoiceableValue( Serializable key, String displayValue ) {
		addChoiceableValue(key, new String[]{displayValue} );
	}
	
	public void addChoiceableValue( Serializable key, String[] displayValues ) {
		myValueList.add( new ChoiceableValueImpl(key, displayValues) );
	}
}
