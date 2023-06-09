package upe.process.impl;

import upe.process.*;
import upe.process.messages.UProcessMessage;
import upe.process.rules.UpeRuleVetoException;

import java.util.ArrayList;
import java.util.List;

public class AbstractUProcessElementImpl implements UProcessElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected UProcessComponent parent = null;
	private String name = null;
	private Boolean visible = true;
	private Boolean enabled = true;
	protected long lastModified = Long.MIN_VALUE;

	public UProcess getProcess() {
		return parent.getProcess();
	}
	
	private List<UProcessMessage> msgList = new ArrayList<>();
	private transient List<UProcessElementListener> listenerList = new ArrayList<>();
	
	Boolean needsRenderung = false;
	/**
	 * Creates a new process element under the parent process component 
	 * and the given name. The constructor connects this new element as
	 * a child under the process component.
	 * 
	 * @param parent
	 * @param name
	 */
	public AbstractUProcessElementImpl(UProcessComponent parent, String name ) {
		setParent(parent);
		setName(name);
		if( parent != null ) {
			parent.addProcessElement(name, this);
		}
	}

	protected void inputStarts() {
		needsRenderung = false;
	}
	
	public String getElementPath() {
		if( parent == null ) {
			return "";
		}
		if( parent instanceof UProcessComponentList<? extends UProcessComponent> list) {
			return parent.getElementPath() + "["+list.indexOf(this)+"]";
		}
		return parent.getElementPath()+"/"+getName();
	}

	public void setParent( UProcessComponent parent ) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean isVisible() {
		return this.visible!=null && this.visible;
	}

	public void setVisible(Boolean value) {
		if( this.visible != value ) {
			this.lastModified = System.currentTimeMillis();
		}
		this.visible = value;
	}

	@Override
	public int getMaximumMessageLevel() {
		if( msgList == null || msgList.isEmpty() ) { 
			return 0;
		}
		int maxLevel = UProcessMessage.MESSAGE_LEVEL_NONE;
		for( UProcessMessage msg : msgList ) {
			if( msg.getMessageLevel() > maxLevel ) {
				maxLevel = msg.getMessageLevel();
			}
		}
		return maxLevel;
	}

	@Override
	public List<UProcessMessage> getMessages() {
		return msgList;
	}
	
	public void addProcessMessage( UProcessMessage msg ) {
		if( msgList.contains(msg) ) {
			return;
		}
		msgList.add( msg );
		needsRenderung = true;
	}
	
	public void removeProcessMessage( UProcessMessage msg ) {
		if( msgList.contains(msg) ) {
			setNeedsRendering(true);
			msgList.remove(msg);
		}
	}

	public boolean needsRendering() {
		return needsRenderung;		
	}
	
	public void setNeedsRendering(Boolean value) {
		needsRenderung = value;
	}

	public boolean isEnabled() {
		return enabled != null && enabled;
	}

	public void setEnabled(Boolean enabled) {
		if( this.enabled != enabled ) {
			this.lastModified = System.currentTimeMillis();
		}
		this.enabled = enabled;
	}
	
	public void addProcessElementListener( UProcessElementListener pel ) {
		listenerList.add(pel);
	}
	
	public void removeProcessElementListener( UProcessElementListener pel ) {
		listenerList.remove(pel);
	}
	
	protected void fireElementChanged() throws UpeRuleVetoException {
		for( UProcessElementListener pel : listenerList ) {
			pel.elementChanged(this);
		}
	}

	@Override
	public boolean modifiedSince(long timeStamp) {
		return this.lastModified > timeStamp;
	}

	@Override
	public void resetModificationTracking() {
		this.lastModified = Long.MIN_VALUE;
	}

	protected void setLastModified(long lmTS) {
		this.lastModified = lmTS;
	}
}
