package br.com.damasceno.agenda.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.damasceno.agenda.constant.Constants;
import br.com.damasceno.agenda.util.SharedPreferencesUtil;
import br.com.damasceno.agenda.util.ToastUtil;

public class SplashActivity extends AppCompatActivity implements Constants {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        String credentials = null;
        credentials = SharedPreferencesUtil.getCredentials(getApplicationContext());

        // Verify if user is logged in
        if(credentials != null) {

            ToastUtil.toast(this, "Tem credenciais salvas : " + credentials);

            // Redirect to Main Activity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;

        } else {

            ToastUtil.toast(this, "Nao tem credenciais salvas");

            // Redirect to Login Activity
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
            finish();
            return;

        }

    }
}
