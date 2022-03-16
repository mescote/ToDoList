package com.example.todolist;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Task {
    private static final String DATE_FORMAT = "dd/MM/yyyy 'at' HH:mm:ss a";
    private String mTitle;
    private Date mDate;

    public Task(String title, Date date) {
        mTitle = title;
        mDate = date;
    }

    private Task(String title, String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ROOT);
        mTitle = title;

        try {
            mDate = dateFormat.parse(date);
        } catch (ParseException e) {}
    }

    public String getTitle() {
        return mTitle;
    }

    public Date getDate() {
        return mDate;
    }

    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ROOT);

        return mTitle + "," +
                dateFormat.format(mDate) + ",";
    }

    public static Task fromString(String task) {
        String[] data = task.split(",", 4);
        return new Task(data[0], data[1]);
    }
}
