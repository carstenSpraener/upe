package upe.process;

import upe.process.rules.UProcessRule;
import upe.process.validation.UProcessValidator;

import java.util.List;

public interface UProcessComponent extends UProcessElement {

	void addProcessElement( String name, UProcessElement pe );
	
	UProcessElement getProcessElement(String name );

	<C extends UProcessElement> C getProcessElement(String name, Class<C> clazz);

	List<UProcessElement> getProcessElements(List<UProcessElement> resultList);
	
	void addValidator( UProcessValidator pv );
	List<UProcessValidator> getValidators();
	void doValidation();

	void addRule(UProcessRule rule);
	List<UProcessRule> getRulesRecursive(List<UProcessRule> collectorList);
}
