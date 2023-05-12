package upe.annotations;

import upe.process.UProcessField;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A class field annotated with this annotation will be handled as a {@link UProcessField}.
 * <p>
 * If you specify a name, this name must be unique in a process. If you don't specify
 * a name, the name of the field will be taken.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UpeProcessField {
    /**
     * The unique name of the field if given. If it is omitted, then the name of the field
     * will be used.
     *
     * @return the assigned name of this field or empty string.
     */
    String value() default "";
}
