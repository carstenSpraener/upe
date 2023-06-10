package upe.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A method annotated with this annotation will be called when
 * the validation cycle is runniing. It needs to return a boolean
 * value of true if and only if the error condition is fullfilled.
 * <p>
 * The value of the annotation is the message-ID of the message to raise
 * when the error occurs.
 * <p>
 * The parameters of the method can be mapped to process values via
 * the UproProcessValue annotation. They have to be Strings as they
 * get the "valueForFrontend" injected.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UpeValidator {
    String value();
}
