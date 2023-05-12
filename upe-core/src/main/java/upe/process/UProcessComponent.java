package upe.process;

import upe.process.validation.UProcessValidator;

import java.util.List;

public interface UProcessComponent extends UProcessElement {

	void addProcessElement( String name, UProcessElement pe );
	
	UProcessElement getProcessElement(String name );

	List<UProcessElement> getProcessElements(List<UProcessElement> resultList);
	
	void addValidator( UProcessValidator pv );
	List<UProcessValidator> getValidators();

	void doValidation();
}
