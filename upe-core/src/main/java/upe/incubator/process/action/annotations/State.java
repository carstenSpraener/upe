package upe.incubator.process.action.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@java.lang.annotation.Target( value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface State {
  String name();
}
