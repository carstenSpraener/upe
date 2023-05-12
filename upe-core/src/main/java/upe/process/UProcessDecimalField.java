package upe.process;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public interface UProcessDecimalField extends UProcessField {
	void setDecimalValue( BigDecimal value );
	BigDecimal getDecimalValue();
	void setFrontendFormat(DecimalFormat format);
}
