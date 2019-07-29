package com.icanstudioz.taxi.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPref {
    private static final String PREF_NAME = "ALAZWAJ";

    public static void saveValue(String key, String value, Context context) {
        SharedPreferences mPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mPreferences.edit().putString(key, value).commit();
    }

    public static String getValue(String key, Context context) {
        SharedPreferences mPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String s = mPreferences.getString(key, "");
        return s;
    }

    public static void saveBoolValue(String key, boolean value, Context context) {
        SharedPreferences mPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mPreferences.edit().putBoolean(key, value).apply();
    }

    public static Boolean getBoolValue(String key, Context context) {
        SharedPreferences mPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean s = mPreferences.getBoolean(key, false);
        return s;
    }

//    public static void saveUser(User user, Context context){
//        SharedPreferences mPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = gson.toJson(user);
//        mPreferences.edit().putString("user", json).commit();
//    }
//
//    public static User getUser(Context context){
//        SharedPreferences mPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = mPreferences.getString("user", null);
//        User user = gson.fromJson(json, User.class);
//        return user;
//    }

}
