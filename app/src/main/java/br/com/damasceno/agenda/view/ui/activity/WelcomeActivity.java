package br.com.damasceno.agenda.view.ui.activity;

import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import br.com.damasceno.agenda.constant.Constants;
import br.com.damasceno.agenda.view.ui.fragment.LoginFragment;
import br.com.damasceno.agenda.view.ui.fragment.RegisterFragment;
import br.com.damasceno.agenda.view.ui.fragment.WelcomeFragment;
import br.com.damasceno.agenda.helper.GlideApp;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends AppCompatActivity implements Constants, WelcomeFragment.OnButtonClickListener {

    @BindView(R.id.main_layout)
    LinearLayout background;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        GlideApp
                .with(this)
                .load(R.drawable.background_office)
                .centerCrop()
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        background.setBackground(resource);
                    }
                });

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
            case R.id.btn_call_login:

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

            case R.id.btn_call_register:

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
