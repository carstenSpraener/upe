package upe.converter;



/**
 * Converters are mainly used to convert a value from the frontend, which is mostly a string,
 * into a value for a dedicated process field and vise versa.
 * <p>
 * Typically, they come in pairs. An XXXToYYY converter and an YYYToXXX converter.
 *
 * @param <I> the input type of this converter
 * @param <O> the output type of this converter
 */
public interface Converter<I , O > {
    O convert(I value) throws ConversionException;
}
