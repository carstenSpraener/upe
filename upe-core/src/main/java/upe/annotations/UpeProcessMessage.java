package upe.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A public static String field annotated with this annotation will
 * register the value of the field as a process message to the
 * Applcation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UpeProcessMessage {
    /**
     * The unique message-ID of the process message
     * @return
     */
    String value();

    /**
     * The severity level of the message. Must be one of the Messlage.LEVEL_-Values.
     * @return
     */
    int level();
}
