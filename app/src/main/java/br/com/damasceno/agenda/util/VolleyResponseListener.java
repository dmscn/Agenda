package br.com.damasceno.agenda.util;

import android.support.annotation.Nullable;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by dmscn on 29/10/17.
 */

public interface VolleyResponseListener {

    void onResponse(@Nullable JSONObject response);
    void onError(@Nullable String statusCode);
}
