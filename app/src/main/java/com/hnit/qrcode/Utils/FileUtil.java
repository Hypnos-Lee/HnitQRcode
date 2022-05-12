package com.hnit.qrcode.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class FileUtil{

    public static void saveString(Context context, String key, String value){
        SharedPreferences.Editor editor = context.getSharedPreferences("userdata", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

    public static void saveInt(Context context, String key, int value){
        SharedPreferences.Editor editor = context.getSharedPreferences("userdata", Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInt(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, -1);
    }
}
