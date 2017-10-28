package br.com.damasceno.agenda.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.icons.MaterialDrawerFont;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import br.com.damasceno.agenda.adapter.PageAdapter;
import br.com.damasceno.agenda.constant.Constants;
import br.com.damasceno.agenda.fragment.ContactFragment;
import br.com.damasceno.agenda.fragment.EventFragment;
import br.com.damasceno.agenda.fragment.TaskFragment;
import br.com.damasceno.agenda.util.SharedPreferencesUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements Constants, TaskFragment.OnFragmentInteractionListener, EventFragment.OnFragmentInteractionListener, ContactFragment.OnFragmentInteractionListener {

    @BindView(R.id.tabs)
    TabLayout tabs;

    @BindView(R.id.pager)
    ViewPager pager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab_task)
    FloatingActionButton fabTask;

    @BindView(R.id.fab_event)
    FloatingActionButton fabEvent;

    @BindView(R.id.fab_contact)
    FloatingActionButton fabContact;

    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        String userName = null;
        String userEmail = null;

        /*
         *  TOOLBAR
         */

        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);


        /*
         *  DRAWER
         */

        // Drawer Items
        PrimaryDrawerItem itemLogout = new PrimaryDrawerItem()
                .withIdentifier(1)
                .withSelectable(false)
                .withIcon(GoogleMaterial.Icon.gmd_square_right)
                .withName(getString(R.string.str_logout));

        PrimaryDrawerItem itemSettings = new PrimaryDrawerItem()
                .withIdentifier(2)
                .withSelectable(false)
                .withIcon(GoogleMaterial.Icon.gmd_settings)
                .withName(getString(R.string.str_settings));

        ProfileDrawerItem profile = new ProfileDrawerItem()
                .withName("Leonardo Damasceno")      // TODO: Get username
                .withEmail("email@email.com");       // TODO: Get email
                //.withIcon()

        // Account Header
        final AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(false)
                .withSavedInstance(savedInstanceState)
                .withThreeSmallProfileImages(true)
                .addProfiles(profile)
                .withHeaderBackground(R.drawable.drawer_background)
                .withSelectionListEnabledForSingleProfile(false)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        return false;
                    }
                }).build();


        // Drawer
        Drawer menu = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(accountHeader)
                .withActionBarDrawerToggleAnimated(true)
                .withSavedInstance(savedInstanceState)
                .addDrawerItems(
                        itemLogout,
                        itemSettings
                )
                .withSelectedItem(-1)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        switch (position) {

                            case 1:
                                userLogout();
                                break;

                            case 2:
                                // TODO: Handle Settings
                                break;

                        }

                        return false;
                    }
                })
                .build();



        /*
         *  TAB LAYOUT
         */

        // Adding tabs to layout
        tabs.addTab(tabs.newTab().setText(FRAG_TASK_TITLE));
        tabs.addTab(tabs.newTab().setText(FRAG_EVENT_TITLE));
        tabs.addTab(tabs.newTab().setText(FRAG_CONTACT_TITLE));
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);

        // Setting up PageAdapter
        final PageAdapter adapter = new PageAdapter(getSupportFragmentManager(), tabs.getTabCount());
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

        // Layout Actions
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                return;
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                return;
            }
        });



        /*
         *  FAB ACTIONS
         */

        // Add new Task
        fabTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewTaskActivity.class);
                startActivity(intent);
            }
        });

        // Add new Event
        fabEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Add new Contact
        fabContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void userLogout() {

        // Prepare Alert to confirm logout
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.msg_confirm_title))
                .setMessage(getString(R.string.msg_confirm_logout))
                .setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_alert_triangle).paddingDp(3))
                .setPositiveButton(getString(R.string.str_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Remove user credentials from SharedPreferences
                        SharedPreferencesUtil.removeSharedPreferences(MainActivity.this.getApplicationContext());

                        // Redirect user to login
                        Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                        finish();
                        return;
                    }
                })
                .setNegativeButton(getString(R.string.str_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

        // Create and emit Alerts
        alert = builder.create();
        alert.show();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
