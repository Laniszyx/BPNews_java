package com.example.navigationdrawerdemo;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Utils {

    public static ColorDrawable[] vibrantLightColorList =
            {
                    new ColorDrawable(Color.parseColor("#ffeead")),
                    new ColorDrawable(Color.parseColor("#93cfb3")),
                    new ColorDrawable(Color.parseColor("#fd7a7a")),
                    new ColorDrawable(Color.parseColor("#faca5f")),
                    new ColorDrawable(Color.parseColor("#1ba798")),
                    new ColorDrawable(Color.parseColor("#6aa9ae")),
                    new ColorDrawable(Color.parseColor("#ffbf27")),
                    new ColorDrawable(Color.parseColor("#d93947"))
            };

    public static ColorDrawable getRandomDrawbleColor() {
        int idx = new Random().nextInt(vibrantLightColorList.length);
        return vibrantLightColorList[idx];
    }

    public static String DateToTimeFormat(String oldstringDate) {
        PrettyTime p = new PrettyTime(new Locale(getCountry()));
        String isTime = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
                    Locale.ENGLISH);
            Date date = sdf.parse(oldstringDate);
            isTime = p.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return isTime;
    }

    public static String DateFormat(String oldstringDate) {
        String newDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, d MMM yyyy", new Locale(getCountry()));
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(oldstringDate);
            newDate = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            newDate = oldstringDate;
        }

        return newDate;
    }

    public static String getCountry() {
        //Locale locale = Locale.getDefault();
        Locale locale = Locale.TAIWAN;
        String country = String.valueOf(locale.getCountry());
        return country.toLowerCase();
    }
    public static String getUSA() {
        Locale locale = Locale.US;
        String country = String.valueOf(locale.getCountry());
        return country.toLowerCase();
    }

    public static String getLanguage(){
        Locale locale = Locale.TAIWAN;
        String country = String.valueOf(locale.getLanguage());
        return country.toLowerCase();
    }
    public static String getEnglish(){
        Locale locale = Locale.US;
        String country = String.valueOf(locale.getLanguage());
        return country.toLowerCase();
    }
    public static String getSports(){
        Locale locale = Locale.TAIWAN;
        String sports = "sports";
        return sports;
    }
    public static String getHealth(){
        Locale locale = Locale.TAIWAN;
        String sports = "health";
        return sports;
    }
    public static String getBusiness(){
        Locale locale = Locale.TAIWAN;
        String sports = "business";
        return sports;
    }
    public static String getScience(){
        Locale locale = Locale.TAIWAN;
        String sports = "science";
        return sports;
    }
    public static String getTechnology(){
        Locale locale = Locale.TAIWAN;
        String sports = "technology";
        return sports;
    }
    public static String getEntertainment(){
        Locale locale = Locale.TAIWAN;
        String sports = "entertainment";
        return sports;
    }

    public static String getSources(){
        Locale locale = Locale.TAIWAN;
        String sources = "google-news";
        return sources;
    }

}
