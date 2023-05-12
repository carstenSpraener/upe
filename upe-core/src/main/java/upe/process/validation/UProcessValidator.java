package upe.process.validation;

import upe.process.UProcessComponent;

import java.io.Serializable;

public interface UProcessValidator extends Serializable {
	void validate(UProcessComponent proc);
}
