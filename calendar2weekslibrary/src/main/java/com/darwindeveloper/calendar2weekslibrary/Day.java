package com.darwindeveloper.calendar2weekslibrary;

import android.graphics.Color;

import java.util.Date;

/**
 * Created by DARWIN on 1/3/2017.
 */

public class Day {
    private Date date;
    private boolean valid, showBadge, header;
    private String textHeader;
    private int paddingHeader;
    private int year, month;
    private int numEvents;
    private int textColor = Color.parseColor("#0099cc");
    private int backgroundColor = Color.parseColor("#FFF5F5F5");
    private int headerBackgroundColor = Color.parseColor("#FFF5F5F5");
    private int colorBadge = Color.parseColor("#0099cc");

    public Day(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Day(Date date) {
        this.date = date;
        valid = true;
    }


    public Day(Date date, boolean valid) {
        this.date = date;
        this.valid = valid;
    }


    public Day(Date date, boolean showBadge, int numEvents) {
        this.date = date;
        this.showBadge = showBadge;
        this.numEvents = numEvents;
        valid = true;
    }


    public Day(Date date, int backgroundColor, int textColor) {
        this.date = date;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        valid = true;
    }

    public Day(Date date, boolean showBadge, int numEvents, int textColor, int backgroundColor) {
        this.date = date;
        this.showBadge = showBadge;
        this.numEvents = numEvents;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        valid = true;
    }

    public Day(Date date, boolean showBadge, int numEvents, int textColor, int backgroundColor, int colorBadge) {
        this.date = date;
        this.showBadge = showBadge;
        this.numEvents = numEvents;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        this.colorBadge = colorBadge;
        valid = true;
    }

    public Day(boolean header, int year, int month, String textHeader) {
        this.header = header;
        this.year = year;
        this.month = month;
        this.textHeader = textHeader;
    }

    public Day(boolean header, int year, int month, String textHeader, int headerBackgroundColor, int paddingHeader) {
        this.header = header;
        this.year = year;
        this.month = month;
        this.textHeader = textHeader;
        this.headerBackgroundColor = headerBackgroundColor;
        this.paddingHeader = paddingHeader;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isShowBadge() {
        return showBadge;
    }

    public void setShowBadge(boolean showBadge) {
        this.showBadge = showBadge;
    }

    public int getNumEvents() {
        return numEvents;
    }

    public void setNumEvents(int numEvents) {
        this.numEvents = numEvents;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getColorBadge() {
        return colorBadge;
    }

    public void setColorBadge(int colorBadge) {
        this.colorBadge = colorBadge;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isHeader() {
        return header;
    }

    public void setHeader(boolean header) {
        this.header = header;
    }

    public String getTextHeader() {
        return textHeader;
    }

    public void setTextHeader(String textHeader) {
        this.textHeader = textHeader;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getHeaderBackgroundColor() {
        return headerBackgroundColor;
    }

    public void setHeaderBackgroundColor(int headerBackgroundColor) {
        this.headerBackgroundColor = headerBackgroundColor;
    }


    public int getPaddingHeader() {
        return paddingHeader;
    }

    public void setPaddingHeader(int paddingHeader) {
        this.paddingHeader = paddingHeader;
    }
}
