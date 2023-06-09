package upe.incubator.process.action.annotations;


import java.lang.reflect.Method;
import java.util.Map;


public class ActionState {
	String name = null;
	transient Method method = null;
	
	public ActionState(String name, Method method) {
		setName(name);
		setMethod(method);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public ActionState getNextState(Object result, Map<String, ActionState> name2StateMap) {
		Transitions annoTransitions = method.getAnnotation(Transitions.class);
		if( annoTransitions == null ) {
			throw new NullPointerException( "No transitions defined for method "+method.getName() );
		}
		Transition[] transitions = annoTransitions.value();
		if( transitions.length==1 ) {
			return name2StateMap.get(transitions[0].target());
		}
		for( int i=0; i < transitions.length; i++ ) {
			if( transitions[i].result().equals(result) ) {
				return name2StateMap.get(transitions[i].target());
			}
		}
		throw new NullPointerException( "No transition defined for value "+result+" in method "+method.getName() );
	}

}
