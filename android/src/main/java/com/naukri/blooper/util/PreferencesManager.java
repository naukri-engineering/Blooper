package com.naukri.blooper.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.naukri.blooper.Blooper;
import com.naukri.blooper.database.DbConstants;


public class PreferencesManager
{
    /**
     * Get shared preference instance.
     * @return SharedPreferences
     */
    private static SharedPreferences getSharedPreference()
    {
        return PreferenceManager.getDefaultSharedPreferences(Blooper.getContext());
    }

    /**
     * Save user id to identify users uniquely
     * @param userId user id
     */
    public static void saveUserId(String userId)
    {
        SharedPreferences sharedPreferences = getSharedPreference();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DbConstants.PREF_UID, userId);
        editor.commit();
    }

    /**
     * Get user id from preferences
     * @return user id
     */
    public static String getUserId()
    {
        SharedPreferences sharedPreferences = getSharedPreference();
        return sharedPreferences.getString(DbConstants.PREF_UID, "");
    }

}
