package upe.process.validation.impl;

import upe.process.UProcessComponent;
import upe.process.UProcessElement;
import upe.process.UProcessElementListener;
import upe.process.UProcessField;
import upe.process.messages.UProcessMessage;
import upe.process.messages.UProcessMessageStorage;
import upe.process.validation.UProcessValidator;

import java.math.BigDecimal;

public abstract class UProcessValidatorSupport implements UProcessValidator, UProcessElementListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void addMessage(String msgID, UProcessComponent proc, String... processFields) {
		UProcessMessage msg = UProcessMessageStorage.getInstance().getMessage(msgID);
		for( int i=0; i<processFields.length; i++ ) {
			proc.getProcessElement(processFields[i]).addProcessMessage(msg);
		}
	}


	protected void removeMessage(String msgID, UProcessComponent proc, String... processFields) {
		UProcessMessage msg = UProcessMessageStorage.getInstance().getMessage(msgID);
		for( int i=0; i<processFields.length; i++ ) {
			proc.getProcessElement(processFields[i]).removeProcessMessage(msg);
		}
	}

	protected int getInt(UProcessComponent proc, String processField) {
		UProcessField pf = (UProcessField)proc.getProcessElement(processField);
		Object value = pf.getValue();
		if( value instanceof Integer i) {
			return i.intValue();
		} else if( value instanceof BigDecimal bd) {
			return bd.intValue();
		} 
		return Integer.parseInt(pf.getValueForFrontend());
	}

	protected String getString(UProcessComponent proc, String processField) {
		UProcessField pf = (UProcessField)proc.getProcessElement(processField);
		Object value = pf.getValue();
		if( value==null ) {
			return null;
		} else {
			return value.toString();
		}
	}

	protected BigDecimal getDecimal(UProcessComponent proc, String elementPath) {
		UProcessField pf = (UProcessField)proc.getProcessElement(elementPath);
		Object value = pf.getValue();
		if( value == null || "".equals(value) ) {
			return null;
		}
		if( value instanceof BigDecimal bd ) {
			return bd;
		}
		return new BigDecimal(pf.getValueForFrontend());
	}

	public void bindToProcessElements( UProcessElement[] elements ) {
		for( int i=0; i<elements.length; i++ ) {
			elements[i].addProcessElementListener(this);
		}
	}
	
	public void elementChanged( UProcessElement anElement ) {
		validate(anElement.getProcess());
	}
}
