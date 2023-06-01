package upe.process.impl;

import upe.exception.UPERuntimeException;
import upe.process.UProcessComponent;
import upe.process.UProcessDecimalField;
import upe.process.UProcessEngine;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

public class UProcessDecimalFieldImpl extends AbstractUProcessFieldImpl implements UProcessDecimalField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final DecimalFormat MONEY_FORMAT   = new DecimalFormat("#,###.00", DecimalFormatSymbols.getInstance(Locale.getDefault()));
	public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("####.##", DecimalFormatSymbols.getInstance(Locale.getDefault()));
	public static final DecimalFormat INTEGER_FORMAT = new DecimalFormat("##", DecimalFormatSymbols.getInstance(Locale.getDefault()));
	
	private DecimalFormat decimalFormat = null;
	
	public UProcessDecimalFieldImpl(UProcessComponent parent, String name) {
		super(parent, name);
	}

	public UProcessDecimalFieldImpl(UProcessComponent parent, String name, DecimalFormat frontendFormat) {
		super(parent, name);
		setDecimalFormat(frontendFormat);
	}

	@Override
	public void setValue( Serializable value ) {
		if( value == null ) {
			super.setValue(null);
			return;
		}
		if( value instanceof BigDecimal ) {
			super.setValue(value);
		} else if( value instanceof Number ) {
			super.setValue( new BigDecimal(value.toString()) );
		} else {
			throw new ClassCastException( "Can not convert "+value+" to big decimal." );
		}
	}

	@Override
	public BigDecimal getDecimalValue() {
		return (BigDecimal)super.getValue();
	}

	@Override
	public void setDecimalValue(BigDecimal value) {
		setValue(value);
	}

	@Override
	public void setValueFromFrontend(String value ) {
		if( value == null ) {
			setDecimalValue(null);
			return;
		}
		try {
			setDecimalValue( new BigDecimal(getDecimalFormat().parse(value).toString()) );
		} catch (ParseException e) {
			throw new UPERuntimeException("can not set "+value+" to a decimal field.",e);
		}
	}

	@Override
	public String getValueForFrontend() {
		BigDecimal decValue = getDecimalValue();
		if( decValue == null ) {
			return "";
		}
		return getDecimalFormat().format(decValue);
	}

	@Override
	public void setFrontendFormat(DecimalFormat format) {
		setDecimalFormat(format);
	}

	public DecimalFormat getDecimalFormat() {
		if( decimalFormat == null ) {
			decimalFormat = MONEY_FORMAT;
			UProcessEngine pe = getProcess().getProcessEngine();
			DecimalFormatSymbols formatSymbols = DecimalFormatSymbols.getInstance(pe.getLocale());
			decimalFormat.setDecimalFormatSymbols(formatSymbols);
		}
		return decimalFormat;
	}

	public void setDecimalFormat(DecimalFormat decimalFormat) {
		this.decimalFormat = decimalFormat;
		UProcessEngine pe = getProcess().getProcessEngine();
		Locale locale = Locale.getDefault();
		if( pe != null ) {
			locale = pe.getLocale();
		}
		DecimalFormatSymbols formatSymbols = DecimalFormatSymbols.getInstance(locale);
		decimalFormat.setDecimalFormatSymbols(formatSymbols);
	}
}
