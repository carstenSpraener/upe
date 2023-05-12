package upe.incubator.process.action.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * marks a method as a call to a action. The value gives the 
 * path to the action to call. The method must return a map 
 * with the args to the action.
 * @author SpC
 *
 */
@java.lang.annotation.Target( value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionCall {
	String actionName();
	String returnState();
}
