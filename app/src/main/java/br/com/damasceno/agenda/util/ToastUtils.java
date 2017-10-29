package br.com.damasceno.agenda.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by dmscn on 18/10/17.
 */

public class ToastUtils {
    public static void toast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
