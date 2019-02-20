package com.newsviewsv2.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

import com.newsviewsv2.R;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Operation {

    public static int height = Resources.getSystem().getDisplayMetrics().heightPixels;
    public static int width = Resources.getSystem().getDisplayMetrics().widthPixels;


    public static long getTimeDifferenceInMillis(String dateTime) {

        //  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        long currentTime = new Date().getTime();
        long endTime = 0;

        try {
            //Parsing the user Inputed time ("yyyy-MM-dd HH:mm:ss")
            endTime = dateFormat.parse(dateTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }

        if (currentTime > endTime)
            return currentTime - endTime;
        else
            return 0;
    }

    public static String getDurationBreakdown(long millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sb = new StringBuilder(64);
        if (days >= 1) {
            sb.append(days);
            sb.append("d ");
        }
        if (hours >= 1) {
            sb.append(hours);
            sb.append("h ");
        }
        long mMinutes = hours * 60;
        if (minutes < 60 && minutes >= 0 && minutes > mMinutes) {

            sb.append(minutes);
            sb.append("m ");
        }

        // sb.append(seconds);
        //sb.append(" Seconds");

        return (sb.toString());
    }


    public static boolean isDate(String str) {
        return str.matches("[+-]?\\d*(\\/\\d+)?");
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static Bitmap getFacebookProfilePicture(String userID){
        Bitmap bitmap = null;
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            URL imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
            bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }


    public static void secondarySplashScreen(View v) {
        /*USE SPLASH SCREEEN*/
//        View v = (View)findViewById(R.id.imageView);

        // Animation animation =new ScaleAnimation(1,5,1,5,.5f,.5f);
        Animation animation = new TranslateAnimation(0, -1500f, 0, 0);
        animation.setStartOffset(500);
        animation.setInterpolator(new AccelerateInterpolator(200));
        animation.setDuration(5000);
        animation.setFillAfter(true);

        Animation animation1 = new AlphaAnimation(1f, 0f);
        animation1.setDuration(2000);
        animation1.setFillAfter(true);


        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(animation);

        animationSet.setDuration(1200);
        animationSet.setFillAfter(true);
        v.startAnimation(animationSet);
        /*USE SPLASH SCREEEN*/
    }
}
