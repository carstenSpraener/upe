package upe.process;


import upe.process.choices.ChoiceSet;

/**
 * This type of field provides a set of choiceable values and a selected value.
 * It is typically used to implement a combo box / select box or a table with
 * listed data. The user can select a value.
 * 
 * 
 * @author casi
 *
 */
public interface UProcessChoiceField extends UProcessField {
	
	ChoiceSet getChoiceSet();
	
	void setChoiceSet( ChoiceSet data );

}
