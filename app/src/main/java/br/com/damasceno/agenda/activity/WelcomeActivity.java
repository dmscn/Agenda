package br.com.damasceno.agenda.activity;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.damasceno.agenda.constant.Constants;
import br.com.damasceno.agenda.fragment.LoginFragment;
import br.com.damasceno.agenda.fragment.RegisterFragment;
import br.com.damasceno.agenda.fragment.WelcomeFragment;
import butterknife.ButterKnife;

public class WelcomeActivity extends AppCompatActivity implements Constants, WelcomeFragment.OnButtonClickListener {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ButterKnife.bind(this);

        fragmentManager = getSupportFragmentManager();

        loadDefaultFragment();
    }

    private void loadDefaultFragment() {
        Fragment fragment;

        fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAG_WELCOME);

        if(fragment == null) {
            fragment = new WelcomeFragment();
        }

        fragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment, TAG_FRAG_WELCOME)
                .commit();
    }

    @Override
    public void OnButtonClicked(@IdRes int id) {
        Fragment fragment;

        switch (id) {
            case R.id.btnCallLogin:

                fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAG_LOGIN);

                if(fragment == null) {
                    fragment = new LoginFragment();
                }

                fragmentManager
                        .beginTransaction()
                        .replace(R.id.container, fragment, TAG_FRAG_LOGIN)
                        .addToBackStack(TAG_FRAG_LOGIN)
                        .commit();

                break;

            case R.id.btnCallRegister:

                fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAG_REGISTER);

                if(fragment == null) {
                    fragment = new RegisterFragment();
                }

                fragmentManager
                        .beginTransaction()
                        .replace(R.id.container, fragment, TAG_FRAG_REGISTER)
                        .addToBackStack(TAG_FRAG_REGISTER)
                        .commit();

                break;
        }
    }
}
