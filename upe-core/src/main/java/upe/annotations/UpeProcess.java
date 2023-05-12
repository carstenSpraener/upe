package upe.annotations;

import upe.process.UProcess;
import upe.process.UProcessEngine;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A class with this annotation is a upe process. It must have a name.
 * <p>
 * The application can call such a process via the {@link UProcessEngine}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UpeProcess {
    /**
     * The unique name of this {@link UProcess}
     * @return
     */
    String value();
}
