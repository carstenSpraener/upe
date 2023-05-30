package upe.test.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A Junit test can mark a field with this annotation and gets a running instance of
 * the required process injected. The type of the field and the type of the process
 * must be compatible.
 *
 * The name of the process is taken from the fields name if not given.
 * <pre>
 * {@code
 *     @UpeProcessToTest("personProcess")
 *     private PersonProcess prsProc;
 * }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UpeProcessToTest {
    String value() default "";
}
