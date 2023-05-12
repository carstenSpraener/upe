package upe.process.choices;

import java.io.Serializable;

/**
 * represents a choice in a ChoiceSet. The key value is a identifier 
 * of this value. The display values are Strings to represent the 
 * value on the frontend.
 * 
 * @author casi
 *
 */
public interface ChoiceableValue extends Serializable {

	/**
	 * returns a unique identifier for this value. It must be unique
	 * about all values in a ChoiceSet.
	 * 
	 * @return the unique identifier.
	 */
	public Serializable getKeyValue();
	
	/**
	 * returns all string values to represent this value on the 
	 * frontend. It can be a single string for a combo box or a array
	 * of Strings for a table.
	 * 
	 * @return an array of at least one string to present the value on the frontend.
	 */
	public String[] getDisplayValues();
}
