package upe.process.impl;

import upe.exception.UPERuntimeException;
import upe.process.UProcessAction;
import upe.process.UProcessComponent;

import java.io.Serializable;
import java.util.Map;

public abstract class AbstractUActionImpl extends AbstractUProcessElementImpl implements UProcessAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean isEnabled;

	protected AbstractUActionImpl(UProcessComponent parent, String name) {
		super(parent, name);
	}

	public Serializable execute(Map<String, Serializable> args) {
		try {
			return internalExecute(args);
		} catch( Exception e ) {
			throw new UPERuntimeException(e);
		}
	}

	@Override
	public boolean isEnabled() {
		return isEnabled;
	}

	@Override
	public void setEnabled( boolean value ) {
		this.isEnabled = value;
	}
	
	public abstract Serializable internalExecute( Map<String, Serializable> args );
}
