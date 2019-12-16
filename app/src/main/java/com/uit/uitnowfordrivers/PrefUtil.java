package com.uit.uitnowfordrivers;

import android.content.Context;
import android.content.SharedPreferences;

// Lưu vào bộ nhớ app
public class PrefUtil {
    public static void savePref(Context c,String key, String value) {
        SharedPreferences p=c.getSharedPreferences("caches",c.MODE_PRIVATE);
        SharedPreferences.Editor edCaches=p.edit();
        edCaches.putString(key, value);
        edCaches.commit();
    }
    public static String loadPref(Context c,String key) {
        SharedPreferences p=c.getSharedPreferences("caches",c.MODE_PRIVATE);
        return p.getString(key,null);
    }
    public static void removePref(Context c,String key) {
        SharedPreferences p=c.getSharedPreferences("caches",c.MODE_PRIVATE);
        SharedPreferences.Editor edCaches=p.edit();
        edCaches.remove(key);
        edCaches.commit();
    }
    public static void clearPref(Context c,String key) {
        SharedPreferences p=c.getSharedPreferences("caches",c.MODE_PRIVATE);
        SharedPreferences.Editor edCaches=p.edit();
        edCaches.clear();
        edCaches.commit();
    }
}
