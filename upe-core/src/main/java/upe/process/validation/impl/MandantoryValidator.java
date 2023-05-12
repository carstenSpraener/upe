package upe.process.validation.impl;

import upe.process.UProcessComponent;
import upe.process.UProcessDecimalField;
import upe.process.UProcessField;
import upe.process.UProcessTextField;
import upe.process.messages.UProcessMessage;
import upe.process.messages.UProcessMessageImpl;
import upe.process.messages.UProcessMessageStorage;

public class MandantoryValidator extends UProcessValidatorSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String MSG_ID="UPE0001";
	
	static {
		UProcessMessageStorage.getInstance().storeMessage(new UProcessMessageImpl(MSG_ID, "Bitte geben Sie einen Wert an.", UProcessMessage.MESSAGE_LEVEL_ERROR));
	}

	private String elementPath = null;
	
	public MandantoryValidator(String elementPath ) {
		this.elementPath = elementPath;
	}
	
	@Override
	public void validate(UProcessComponent proc) {
		UProcessField pf = (UProcessField) proc.getProcessElement(elementPath);
		if( pf.getValue() == null ) {
			addMessage(MSG_ID, proc, elementPath);
		} else if( pf instanceof UProcessTextField && "".equals(pf.getValue()) ) {
			addMessage(MSG_ID, proc, elementPath);
		} else if( pf instanceof UProcessDecimalField pdf && pdf.getDecimalValue().doubleValue()==0 ) {
			addMessage(MSG_ID, proc, elementPath);
		} else {
			removeMessage(MSG_ID, proc, elementPath);
		}
	}

}
