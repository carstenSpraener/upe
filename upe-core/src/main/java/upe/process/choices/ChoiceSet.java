package upe.process.choices;

import java.io.Serializable;
import java.util.List;


public interface ChoiceSet extends Serializable {
	public List<ChoiceableValue> getChoiceableValues();
	
}
