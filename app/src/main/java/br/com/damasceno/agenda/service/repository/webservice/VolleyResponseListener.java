package br.com.damasceno.agenda.service.repository.webservice;

import android.support.annotation.Nullable;

public interface VolleyResponseListener <T> {

    void onResponse(@Nullable T response);
    void onError(@Nullable String error);
}
