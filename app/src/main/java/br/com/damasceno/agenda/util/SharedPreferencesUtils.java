package br.com.damasceno.agenda.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import br.com.damasceno.agenda.constant.Constants;

public class SharedPreferencesUtils implements Constants {

    public static void storeCredentials(Context context, String credentialsToken) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_CREDENTIALS_TOKEN, credentialsToken);
        editor.apply();
    }

    public static String getCredentials(Context context) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String credentials = sharedPreferences.getString(KEY_CREDENTIALS_TOKEN, null);

        return credentials;
    }

    public static void removeCredentials(Context context) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(KEY_CREDENTIALS_TOKEN);
        editor.apply();
    }

    public static void storeUserProfile(Context context, String name, String email, String picture) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_PICTURE, picture);

        editor.apply();
    }

    public static HashMap<String, String> getUserProfile(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        HashMap<String, String> userProfile = new HashMap<String, String>();

        userProfile.put(KEY_USER_NAME, sharedPreferences.getString(KEY_USER_NAME, null));
        userProfile.put(KEY_USER_EMAIL, sharedPreferences.getString(KEY_USER_EMAIL, null));
        userProfile.put(KEY_USER_PICTURE, sharedPreferences.getString(KEY_USER_PICTURE, null));

        return userProfile;
    }
}
