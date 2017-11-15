package br.com.damasceno.agenda.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import br.com.damasceno.agenda.service.repository.LoginRepository;

public class LoginViewModel extends AndroidViewModel {

    private LoginRepository mLoginRepository;


    public LoginViewModel(@NonNull Application application) {
        super(application);

        mLoginRepository = LoginRepository.getInstance(getApplication());
    }
}
