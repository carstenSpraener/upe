package upe.converter;



/**
 * Converts a object to a string by calling its "toString" method. A null value wil
 * result in a null string.
 */
public class Object2StringConverter implements Converter<Object, String> {

	@Override
	public String convert(Object value) throws ConversionException {
		if( value == null ) {
			return null;
		} else {
			return value.toString();
		}
	}

}
