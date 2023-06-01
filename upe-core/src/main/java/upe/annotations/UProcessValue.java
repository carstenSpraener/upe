package upe.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface UProcessValue {
    /**
     * The path to an ProcessField for the rule to trigger
     * @return
     */
    String value();
}
