package upe.process;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public interface UProcessDateField extends UProcessField {
	DateFormat SIMPLE_FORMAT = new SimpleDateFormat("MM.dd.yyyy");
	DateFormat GERMAN_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
	DateFormat YEAR_FORMAT = new SimpleDateFormat("yyyy");

	void setDateValue( Date value );
	Date getDateValue();
	void setFrontendFormat(DateFormat format);
}
