package upe.annotations;


import upe.process.UProcessAction;
import upe.process.UProcessComponent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A method with this annotation will be handled as a {@link UProcessAction}
 * <p>
 * There can be several UpeProcessAction annotated methods per class, but the name of
 * the action must be unique in a process.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface UpeProcessAction {
    /**
     * The unique name of this action. The name
     * must be unique inside the {@link UProcessComponent} but it can
     * override process actions of its super classes.
     *
     * @return the name of the action
     */
    String value();
}
