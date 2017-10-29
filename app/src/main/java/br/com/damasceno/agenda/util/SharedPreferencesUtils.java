package br.com.damasceno.agenda.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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

    public static void removeSharedPreferences(Context context) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(KEY_CREDENTIALS_TOKEN);
        editor.apply();
    }
}
