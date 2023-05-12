package upe.converter;

import java.io.Serializable;

/**
 * The no conversion at all converter.
 */
public class NoConversionConverter implements Converter<Serializable,Serializable> {

	@Override
	public Serializable convert(Serializable value) throws ConversionException {
		return value;
	}

}
