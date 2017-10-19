package br.com.damasceno.agenda.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dmscn on 18/10/17.
 */

public class SharedPreferencesUtil {

    public static void setPreferenciaString(Context context, String nomePreferencia, String chave, String valor) {

        SharedPreferences.Editor editor = context.getSharedPreferences(nomePreferencia, Context.MODE_PRIVATE).edit();

        editor.putString(chave, valor);
        editor.commit();
    }

    public static String getPreferenciaString(Context context, String nomePreferencia, String chave) {

        String strValor;

        SharedPreferences preferences = context.getSharedPreferences(nomePreferencia, Context.MODE_PRIVATE);
        strValor = preferences.getString(chave, null);

        return strValor;
    }
}
