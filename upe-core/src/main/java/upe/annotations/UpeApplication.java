package upe.annotations;

import upe.process.UProcess;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A UPEApplication is the root class of your application running with upe.
 *
 * You can specify your {@link UProcess} classes here to make them available in UPE.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface UpeApplication {
    /**
     * Holds the list of {@link UProcess} implementations. All these
     * Processes building your application.
     *
     * @return a list of classes annotated with {@link UpeProcess}
     */
    Class<? extends UProcess>[] value();
}
