package upe.converter;

/**
 * Converts a String into a Boolean value. A empty string or a null value
 * is returned as a null value. So the result can be true, false or null.
 * <p>
 * See implementation for strings that are recognized as true values. The
 * comparison is not case sensitiv.
 * <p>
 * All other strings are interpreted as false.
 */
@SuppressWarnings("java:S2447")
public class StringToBooleanConverter implements Converter<String, Boolean> {
    @Override
    public Boolean convert(String value) throws ConversionException {
        if (value == null || "".equals(value)) {
            return null;
        }
        if (
                "Y".equalsIgnoreCase(value) ||
                        "YES".equalsIgnoreCase(value) ||
                        "J".equalsIgnoreCase(value) ||
                        "JA".equalsIgnoreCase(value) ||
                        "1".equals(value) ||
                        "true".equalsIgnoreCase(value)
        ) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
}
