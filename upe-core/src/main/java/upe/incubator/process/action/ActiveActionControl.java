package upe.incubator.process.action;

import upe.incubator.process.action.annotations.ActionState;


import java.util.Map;

public class ActiveActionControl {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private transient ControlableAction actionToControll;
	private transient Map<String, ActionState> name2StateMap;
	private transient ActionState activeState;

	public ActiveActionControl(ControlableAction actionToControll,
			Map<String, ActionState> name2StateMap, ActionState activeState) {
		super();
		this.actionToControll = actionToControll;
		this.name2StateMap = name2StateMap;
		this.activeState = activeState;
	}
	
	public ControlableAction getActionToControll() {
		return actionToControll;
	}
	public void setActionToControll(ControlableAction actionToControll) {
		this.actionToControll = actionToControll;
	}
	public Map<String, ActionState> getName2StateMap() {
		return name2StateMap;
	}
	public void setName2StateMap(Map<String, ActionState> name2StateMap) {
		this.name2StateMap = name2StateMap;
	}
	public ActionState getActiveState() {
		return activeState;
	}
	public void setActiveState(ActionState activeState) {
		this.activeState = activeState;
	}
	
}
