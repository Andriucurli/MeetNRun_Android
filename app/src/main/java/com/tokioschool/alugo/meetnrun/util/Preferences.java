package com.tokioschool.alugo.meetnrun.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {

    static final String PREF_USER_ID= "user_id";
    static final String PREF_NOTIFICATIONS_ENABLED= "notifications_enabled";
    static final String PREF_RECORDATORIES_AUTO= "recordatories_auto";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void set_user_id(Context ctx, int id)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(PREF_USER_ID, id);
        editor.commit();
    }

    public static int get_user_id(Context ctx)
    {
        return getSharedPreferences(ctx).getInt(PREF_USER_ID, -1);
    }

    public static boolean get_notifications_enabled(Context context){
        return getSharedPreferences(context).getBoolean(PREF_NOTIFICATIONS_ENABLED, true);
    }

    public static boolean get_recordatories_auto(Context context){
        return getSharedPreferences(context).getBoolean(PREF_RECORDATORIES_AUTO, false);
    }

    public static void set_preferences(Context context, boolean notifications_enabled, boolean recordatories_auto){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(PREF_NOTIFICATIONS_ENABLED, notifications_enabled);
        editor.putBoolean(PREF_RECORDATORIES_AUTO, recordatories_auto);
        editor.commit();
    }
}
