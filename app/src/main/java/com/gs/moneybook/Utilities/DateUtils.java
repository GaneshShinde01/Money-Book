package com.gs.moneybook.Utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    // Method to format a date into "yyyy-MM-dd" format (for database usage)
    public static String formatDateForDatabase(int day, int month, int year) {
        return String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, day);
    }

    // Method to get the current date in "yyyy-MM-dd" format (for database usage)
    public static String getCurrentDateForDatabase() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        return formatDateForDatabase(day, month, year);
    }

    // Method to format a date and time into "yyyy-MM-dd HH:mm:ss" format (for database usage)
    public static String formatDateTimeForDatabase(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(date);
    }

    // Method to get the current date and time in "yyyy-MM-dd HH:mm:ss" format (for database usage)
    public static String getCurrentDateTimeForDatabase() {
        return formatDateTimeForDatabase(new Date());
    }

    // Method to get current time in HH:mm:ss format
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    // Method to parse date from a string in "yyyy-MM-dd" format
    public static Date parseDateFromDatabase(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;  // Handle the error and return null if parsing fails
        }
    }
}
