package upe.converter;



/**
 * The no conversion at all converter.
 */
public class NoConversionConverter implements Converter<Object,Object> {

	@Override
	public Object convert(Object value) throws ConversionException {
		return value;
	}

}
