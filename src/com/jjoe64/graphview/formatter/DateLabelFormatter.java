package com.jjoe64.graphview.formatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class DateLabelFormatter implements LabelFormatter{
	String format;
	
	public DateLabelFormatter() {
		format = "d MMM kk:mm";
	}
	public DateLabelFormatter(String format) {
		this.format = format;
	}
	@Override
	public String formatLabel(double value) {
		long millis = Math.round(value) * 1000 * 60;
		DateFormat formatter = new SimpleDateFormat(format, Locale.US);
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

	    // Create a calendar object that will convert the date and time value in milliseconds to date. 
		Calendar calendar = Calendar.getInstance();
	    calendar.setTimeInMillis(millis);
	    return formatter.format(calendar.getTime());
	}
}
