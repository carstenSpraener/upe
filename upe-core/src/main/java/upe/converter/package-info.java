/**
 * Converters are mainly used to convert a value from the frontend, which is mostly a string,
 * into a value for a dedicated process field and vise versa.
 * <p>
 * Typically, they come in pairs. An XXXToYYY converter and an YYYToXXX converter.
 * <p>
 * They are most seen in the "setValueFromFrontend" and "getValueForFrontend" methods of the
 * ProcessFieldImpl classes.
 */
package upe.converter;
