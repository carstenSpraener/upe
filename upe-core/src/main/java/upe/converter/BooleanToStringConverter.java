package upe.converter;

/**
 * Converts a boolean value into a string. A null value will result in an empty string.
 * <p>
 * The default constructor make the converter returning the strings 'true' or 'false'.
 * Other strings can be given to a parametrized constructor.
 */
public class BooleanToStringConverter implements Converter<Boolean, String> {
    private final String trueString;
    private final String falseString;

    /**
     * Sets the true and false strings for this converter.
     * @param trueString the string to return on a true value
     * @param falseString the string to return on a false value
     */
    public BooleanToStringConverter(String trueString, String falseString) {
        this.trueString = trueString;
        this.falseString = falseString;
    }

    public BooleanToStringConverter() {
        this("true", "false");
    }

    @Override
    public String convert(Boolean value) throws ConversionException {
        if (value == null) {
            return "";
        }
        return Boolean.TRUE.equals(value) ? this.trueString : this.falseString;
    }
}
