package upe.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A method annotated with this annotation will be called if the containing process
 * is started.
 * <p>
 * The Method must be a method of a process class. (Annotated with @UpeProcess)
 * <p>
 * There could only be one method with this annotation in a process.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UpeOnInitialize {
}
