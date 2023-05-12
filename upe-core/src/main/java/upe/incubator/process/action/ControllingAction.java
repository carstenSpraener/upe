package upe.incubator.process.action;

import upe.exception.UPERuntimeException;
import upe.incubator.process.action.annotations.*;
import upe.process.UProcessAction;
import upe.process.UProcessComponent;
import upe.process.impl.AbstractUActionImpl;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@SuppressWarnings("java:S1149")
public class ControllingAction extends AbstractUActionImpl {
	private static final String START_STATE_NAME = "__START__";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("java:S1948")
	private Map<String, ActionState> myName2StateMap = null;
	@SuppressWarnings("java:S1948")
	private ControlableAction myActionToControl = null;
	
	public ControllingAction(UProcessComponent parent, String name, ControlableAction actionToControll) {
		super(parent, name);
		myActionToControl = actionToControll;
		constructStateMachine(actionToControll);
	}

	private void constructStateMachine(ControlableAction action) {
		List<Method> cntrlMethods = readAnnotatedMethods(action.getClass());
		myName2StateMap = createName2StateMap(cntrlMethods);
	}

	protected Map<String, ActionState> createName2StateMap(List<Method> cntrlMethods) {
		Map<String, ActionState> name2StateMap = new HashMap<>();
		for( Method method : cntrlMethods ) {
			State state = method.getAnnotation(State.class);
			if( state != null ) {
				String name = state.name();
				name2StateMap.put( name, new ActionState(name, method) );
			}
			StartState start = method.getAnnotation(StartState.class);
			if( start != null ) {
				ActionState actState = new ActionState(START_STATE_NAME,method);
				name2StateMap.put( START_STATE_NAME, actState );
				if( start.name() != null ) {
					name2StateMap.put(start.name(), actState);
				}
			}
			TerminationState endPoint = method.getAnnotation(TerminationState.class);
			if( endPoint != null ) {
				String name = endPoint.name();
				name2StateMap.put( name, new ActionState(name,method) );
			}
		}
		return name2StateMap;
	}

	protected List<Method> readAnnotatedMethods(Class<?> c) {
		List<Method> cntrlMethods = new ArrayList<>();
		while( c != Object.class ) {
			Method[] methods = c.getMethods();
			for( int i=0; i<methods.length; i++ ) {
				State state = methods[i].getAnnotation(State.class);
				if( state != null ) {
					cntrlMethods.add(methods[i]);
				}
				StartState start = methods[i].getAnnotation(StartState.class);
				if( start != null ) {
					cntrlMethods.add(methods[i]);
				}
				TerminationState endPoint = methods[i].getAnnotation(TerminationState.class);
				if( endPoint != null ) {
					cntrlMethods.add(methods[i]);
				}
			}
			c = c.getSuperclass();
		}
		return cntrlMethods;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Serializable internalExecute(Map<String, Serializable> args) {
		try {
			// The first call to a cascade of Controlled Actions has to
			// push itself to the control stack.
			ensureStarted();

			while (!getActionStack().isEmpty()) {
				ActiveActionControl activeCntrl = getActionStack().peek();

				ActionState activeState = getActionStack().peek().getActiveState();
				ControlableAction actionToControll = activeCntrl.getActionToControll();
				Map<String, ActionState> name2StateMap = activeCntrl.getName2StateMap();

				Object result = callStateMethod(args, activeState, actionToControll);
				// handle the different method types
				// Call to another process
				if (activeState.getMethod().isAnnotationPresent(ProcessCall.class) ) {
					handleCallProcessAction(activeCntrl, activeState, name2StateMap, (Map<String, Serializable>) result);
					break;
				}
				// Call to another action
				if (activeState.getMethod().isAnnotationPresent(ActionCall.class) ) {
					boolean subActionStarted = handleActionCallAction(activeCntrl, activeState, name2StateMap, (Map<String, Serializable>) result);
					if (!subActionStarted) {
						// temination of a controled action
						if (activeState.getMethod().isAnnotationPresent(TerminationState.class)) {
							getActionStack().pop();
						} else {
							activeCntrl.setActiveState(activeState.getNextState(result, name2StateMap));
						}
					}
				}
			}
			return null;
		} catch( Exception e ) {
			throw new UPERuntimeException(e);
		}
	}

	private boolean handleActionCallAction(ActiveActionControl activeCntrl, ActionState activeState, Map<String, ActionState> name2StateMap, Map<String, Serializable> result) {
		ActionCall actionCall = activeState.getMethod().getAnnotation(ActionCall.class);
		UProcessAction act = (UProcessAction) getProcess().getProcessElement(actionCall.actionName());
		activeCntrl.setActiveState(name2StateMap.get(actionCall.returnState()));
		if (act instanceof ControllingAction cntrAct) {
			ActionState start = cntrAct.myName2StateMap.get(START_STATE_NAME);
			cntrAct.myActionToControl.initialize(getProcess());
			pushAction(cntrAct.myActionToControl, cntrAct.myName2StateMap, start);
			return true;
		} else {
			act.execute(result);
		}
		return false;
	}

	private void handleCallProcessAction(ActiveActionControl activeCntrl, ActionState activeState, Map<String, ActionState> name2StateMap, Map<String, Serializable> result) {
		ProcessCall procCall = activeState.getMethod().getAnnotation(ProcessCall.class);
		String processName = procCall.processName();
		activeCntrl.setActiveState(name2StateMap.get(procCall.returnState()));
		getProcess().getProcessEngine().callProcess(processName, result, this);
	}

	private static Object callStateMethod(Map<String, Serializable> args, ActionState activeState, ControlableAction actionToControll) throws IllegalAccessException, InvocationTargetException {
		Object result = null;
		if (activeState.getMethod().getParameterTypes().length > 0) {
			result = activeState.getMethod().invoke(actionToControll, args);
		} else {
			result = activeState.getMethod().invoke(actionToControll);
		}
		return result;
	}

	private void ensureStarted() {
		if (getActionStack().isEmpty()) {
			ActionState start = myName2StateMap.get(START_STATE_NAME);
			if (start == null) {
				throw new NullPointerException(
						"No start state defined in action "
								+ myActionToControl.getClass().getName());
			}
			pushAction(myActionToControl, myName2StateMap, start);
			myActionToControl.initialize(getProcess());
		}
	}

	private void pushAction(ControlableAction actionToControl, Map<String, ActionState>name2ActionStateMap, ActionState activeState) {
		Stack<ActiveActionControl> actionStack = getActionStack();
		actionStack.push(new ActiveActionControl(actionToControl,name2ActionStateMap,activeState));
	}

	@SuppressWarnings("unchecked")
	private Stack<ActiveActionControl> getActionStack() {
		Stack<ActiveActionControl> actionStack = (Stack<ActiveActionControl>) getProcess().getProcessEngine().getSession().getValue("UPE::ActionStack");
		if( actionStack==null ) {
			actionStack = new Stack<>();
			getProcess().getProcessEngine().getSession().setValue("UPE::ActionStack",actionStack);
		}
		return actionStack;
	}

}
