package upe.converter;

import upe.exception.UPERuntimeException;

/**
 * A UPERuntimeException, showing that a value could not be converted to the
 * required target type.
 */
public class ConversionException extends UPERuntimeException {
	private static final long serialVersionUID = 1L;

	public ConversionException(String msg, Exception nfe) {
		super(msg, nfe);
	}

}
