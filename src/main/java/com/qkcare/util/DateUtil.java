package com.qkcare.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	public static String DATE_TIME_WITHOUT_SECONDS = "yyyy-MM-dd hh:mm";
	public static String DATE_FORMAT = "yyyy-MM-dd";
	public static String TIME_WITHOUT_SECONDS_FORMAT = "hh:mm";
	
	public static Date addOneDay(Date date) {
		return addDays(date, 1);
	}

	public static Date addDays(Date date, int numberOfDays) {
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(date); 
		cal.add(Calendar.DATE, numberOfDays);
		return cal.getTime();
	}
	
	public static String formatDate(Date date, String formatString) {
		SimpleDateFormat dt1 = new SimpleDateFormat(formatString);
        return dt1.format(date);
	}
	
	public static String formatDate(LocalDateTime date, String formatString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatString);
        return date.format(formatter);
	}
	
	public static Date parseDate(String dateStr, String formatString) throws ParseException {
		SimpleDateFormat dt1 = new SimpleDateFormat(formatString);
        return dt1.parse(dateStr);
	}
	
	public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
 
    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
 
    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }
 
    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
	
}
