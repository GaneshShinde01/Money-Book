package com.gs.moneybook.Utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

    // Method to format a date and time into "dd/MM/yyyy HH:mm:ss" format
    public static String formatDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        return sdf.format(date);
    }

    // Method to get the current date and time in "dd/MM/yyyy HH:mm:ss" format
    public static String getCurrentDateTime() {
        return formatDateTime(new Date());
    }

    //Method to get current time in HH:mm:ss format
    public static String getCurrentTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    //method to format date and time in any format you want.
    public static String formatDateTime(Date date, String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(date);
    }
}