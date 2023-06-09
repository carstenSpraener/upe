package upe.process.impl;

import upe.exception.UPERuntimeException;
import upe.process.UProcessComponent;
import upe.process.UProcessField;
import upe.process.rules.UpeRuleVetoException;

import java.io.Serializable;

public class AbstractUProcessFieldImpl extends AbstractUProcessElementImpl
		implements UProcessField {

	private Serializable value = null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AbstractUProcessFieldImpl(UProcessComponent parent, String name) {
		super(parent, name);
	}

	public String getValueForFrontend() {
		if( value == null ) {
			return "";
		} else {
			return value.toString();
		}
	}

	public Serializable getValue() {
		return value;
	}

	public void setValueFromFrontend(String displayValue ) {
		setValue( displayValue );
	}

	@Override
	public void setValue(Serializable newValue) {
		if( value==null && newValue == null) {
			return;
		}
		Serializable oldValue = this.value;
		try {
			if (value == null) {
				value = newValue;
				super.fireElementChanged();
				this.lastModified = System.currentTimeMillis();
				setNeedsRendering(true);
				return;
			}
			if (!value.equals(newValue)) {
				this.value = newValue;
				super.fireElementChanged();
				setNeedsRendering(true);
				this.lastModified = System.currentTimeMillis();
			}
			value = newValue;
		} catch( UpeRuleVetoException ruleVetoException ) {
			this.value = oldValue;
		}
	}

}
