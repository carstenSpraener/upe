package upe.process.impl;

import upe.exception.UPERuntimeException;
import upe.process.UProcessComponent;
import upe.process.UProcessDateField;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class UProcessDateFieldImpl extends AbstractUProcessFieldImpl implements UProcessDateField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DateFormat myFormat = SIMPLE_FORMAT;
	
	public UProcessDateFieldImpl(UProcessComponent parent, String name) {
		super(parent, name);
	}

	public UProcessDateFieldImpl(UProcessComponent parent, String name, DateFormat frontendFormat) {
		super(parent, name);
		this.myFormat = frontendFormat;
	}

	@Override
	public void setValue( Serializable value ) {
		if( value == null ) {
			super.setValue(null);
			return;
		}
		if( value instanceof Date ) {
			super.setValue(value);
		} else if( value instanceof String strValue) {
			try {
				super.setValue( myFormat.parse(strValue) );
			} catch( ParseException pExc ) {
				throw new IllegalArgumentException(
						String.format("Can not parse '%s' as a date.",value));
			}
		} else {
			throw new IllegalArgumentException(String.format("Don't know how to set '%s'  as a date.",value));
		}
	}

	@Override
	public Date getDateValue() {
		return (Date)super.getValue();
	}

	@Override
	public void setDateValue(Date value) {
		setValue(value);
	}

	@Override
	public void setValueFromFrontend(String value ) {
		try {
			setDateValue( myFormat.parse(value) );
		} catch (ParseException e) {
			throw new UPERuntimeException(String.format("Can not set '%s' as a date.",value),e);
		}
	}

	@Override
	public String getValueForFrontend() {
		return myFormat.format(getDateValue());
	}

	@Override
	public void setFrontendFormat(DateFormat format) {
		myFormat = format;
	}
}
