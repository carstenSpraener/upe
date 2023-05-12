package upe.converter;

import java.math.BigDecimal;

/**
 * Converts a String to a BigDecimal value. A null or empty String will result in a null value.
 *
 */
public class String2DecimalConverter implements Converter<String, BigDecimal> {

	public BigDecimal convert(String value) throws ConversionException {
		if( value == null || "".equals(value)) {
			return null;
		}
		try {
			return new BigDecimal(value);
		} catch( NumberFormatException nfe ) {
			throw new ConversionException( "The value "+value+" can not be converted to a decimal.", nfe);
		}
	}

}
