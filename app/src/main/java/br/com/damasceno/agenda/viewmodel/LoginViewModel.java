package br.com.damasceno.agenda.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.util.Base64;

import com.android.volley.Response;

import br.com.damasceno.agenda.service.model.User;
import br.com.damasceno.agenda.service.repository.LoginRepository;

public class LoginViewModel extends AndroidViewModel {

    private LoginRepository mLoginRepository;


    public LoginViewModel(@NonNull Application application) {
        super(application);

        mLoginRepository = LoginRepository.getInstance(getApplication());
    }

    public void login(String username, String password, final Response.Listener<Integer> listener) {

        // Generating the credentials token
        String credentialsToken = "Basic " + Base64.encodeToString((username + ":" + password).getBytes(), Base64.DEFAULT);

        mLoginRepository.login(credentialsToken, response -> listener.onResponse(response));
    }

    public void register(User user, final Response.Listener<Integer> listener) {

        mLoginRepository.register(user, response -> listener.onResponse(response));

    }
}
