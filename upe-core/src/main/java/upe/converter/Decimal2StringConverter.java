package upe.converter;

import java.math.BigDecimal;

/**
 * Converts a BigDecimal to a string value by simply calling "toString"-Method. A null
 * value will result in a null string.
 */
public class Decimal2StringConverter implements Converter<BigDecimal, String> {

	public String convert(BigDecimal value) throws ConversionException {
		if( value==null ) {
			return null;
		}
		return value.toString();
	}

}
