package com.gs.moneybook.Utilities;

import java.util.Calendar;
import java.util.Locale;

public class DateUtils {

    // Method to format a date into "dd/MM/yyyy" format
    public static String formatDate(int day, int month, int year) {
        return String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month + 1, year);
    }

    // Method to get the current date in "dd/MM/yyyy" format
    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        return formatDate(day, month, year);
    }
}
