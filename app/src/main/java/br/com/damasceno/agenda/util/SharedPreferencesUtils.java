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


    /*
     *   PROFILE
     */
    public static void storeUserProfile(Context context, String credentialsToken, String userAcessToken, String name, String email, String picture) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_CREDENTIALS_TOKEN, credentialsToken);
        editor.putString(KEY_USER_ACCESS_TOKEN, userAcessToken);
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

    public static String getCredentials(Context context) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String credentials = sharedPreferences.getString(KEY_CREDENTIALS_TOKEN, null);

        return credentials;
    }

    public static String getUserAccessToken(Context context) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String token = sharedPreferences.getString(KEY_USER_ACCESS_TOKEN, null);

        return token;
    }

    public static void removeUserProfile(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(KEY_CREDENTIALS_TOKEN);
        editor.remove(KEY_USER_NAME);
        editor.remove(KEY_USER_EMAIL);
        editor.remove(KEY_USER_PICTURE);

        editor.apply();
    }

}
