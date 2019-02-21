package com.newsviewsv2.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static com.newsviewsv2.SigninActivity.USER_ID_KEY;

/**
 * Created by Lincoln on 05/05/16.
 */
public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "NewsViewsV2-welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_SECOND_TIME_LAUNCH = "IsSecondTimeLaunch";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.apply();
    }
    public void setSecondTimeLaunch(boolean isSecondtTime) {
        editor.putBoolean(IS_SECOND_TIME_LAUNCH, isSecondtTime);
        editor.apply();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public boolean isSecondTimeLaunch() {
        return pref.getBoolean(IS_SECOND_TIME_LAUNCH, false);
    }


    public  boolean getSwitchStatus(String keyValue,boolean defaultValue){
        return pref.getBoolean(keyValue, defaultValue);
    }
    public void setStatus(String key,boolean defaultValue){
        editor.putBoolean(key,defaultValue);
    }

    public void setUserId(String userId){
        editor.putString(USER_ID_KEY, userId);
        editor.apply();
    }
    public String getUserId(){
        return pref.getString(USER_ID_KEY,"");
    }
}
