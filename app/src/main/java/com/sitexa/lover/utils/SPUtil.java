package com.sitexa.lover.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Locale;

/**
 * Created by pocketEos on 2018/5/23.
 */

public class SPUtil {

    private final String SP_NAME = "language_setting";
    private final String TAG_LANGUAGE = "language_select";
    private static volatile SPUtil instance;

    private final SharedPreferences mSharedPreferences;

    private Locale systemCurrentLocal = Locale.ENGLISH;


    public SPUtil(Context context) {
        mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }


    public void saveLanguage(int select) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putInt(TAG_LANGUAGE, select);
        edit.commit();
    }

    public int getSelectLanguage() {
        return mSharedPreferences.getInt(TAG_LANGUAGE, 0);
    }


    public Locale getSystemCurrentLocal() {
        return systemCurrentLocal;
    }

    public void setSystemCurrentLocal(Locale local) {
        systemCurrentLocal = local;
    }

    public static SPUtil getInstance(Context context) {
        if (instance == null) {
            synchronized (SPUtil.class) {
                if (instance == null) {
                    instance = new SPUtil(context);
                }
            }
        }
        return instance;
    }
}
