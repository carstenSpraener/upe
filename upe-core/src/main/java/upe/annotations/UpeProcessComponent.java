package upe.annotations;

import upe.incubator.process.impl.GenericUProcessImpl;
import upe.process.UProcess;
import upe.process.UProcessComponent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A field inside a {@link UProcess} or {@link UProcessComponent} with
 * this annotation references a subcomponent of this {@link UProcessComponent}.
 * <p>
 * There can be several ProcessComponents inside a Process or UProcessComponent. But the name
 * must be unique. If you have a list of subcomponents, the name of each subcomponent will be
 * <code>name[i]</code> starting with 0.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UpeProcessComponent {
    /**
     * The unique name of the subcomponent inside it's parent component. The name
     * must be unique inside the {@link UProcessComponent} but it can
     * override process components of it's super classes.
     * @return
     */
    String value() default "";
    Class<? extends UProcessComponent> listType() default GenericUProcessImpl.class;
}
