package br.com.damasceno.agenda.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.damasceno.agenda.constant.Constants;
import br.com.damasceno.agenda.fragment.TaskListFragment;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements Constants {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        fragmentManager = getSupportFragmentManager();

        loadInitialFragment();
    }

    private void loadInitialFragment() {
        Fragment fragment;

        fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAG_WELCOME);

        if(fragment == null) {
            fragment = new TaskListFragment();
        }

        fragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment, TAG_FRAG_WELCOME)
                .commit();
    }
}
