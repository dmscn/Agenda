package br.com.damasceno.agenda.activity;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.damasceno.agenda.adapter.PageAdapter;
import br.com.damasceno.agenda.constant.Constants;
import br.com.damasceno.agenda.fragment.ContactFragment;
import br.com.damasceno.agenda.fragment.EventFragment;
import br.com.damasceno.agenda.fragment.TaskFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements Constants, TaskFragment.OnFragmentInteractionListener, EventFragment.OnFragmentInteractionListener, ContactFragment.OnFragmentInteractionListener {

    @BindView(R.id.tabs)
    TabLayout tabs;

    @BindView(R.id.pager)
    ViewPager pager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        tabs.addTab(tabs.newTab().setText(FRAG_TASK_TITLE));
        tabs.addTab(tabs.newTab().setText(FRAG_EVENT_TITLE));
        tabs.addTab(tabs.newTab().setText(FRAG_CONTACT_TITLE));
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);

        final PageAdapter adapter = new PageAdapter(getSupportFragmentManager(), tabs.getTabCount());
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
