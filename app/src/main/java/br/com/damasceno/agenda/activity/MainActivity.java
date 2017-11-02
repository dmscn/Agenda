package br.com.damasceno.agenda.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.github.clans.fab.FloatingActionButton;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;

import java.util.HashMap;
import java.util.List;

import br.com.damasceno.agenda.adapter.PageAdapter;
import br.com.damasceno.agenda.constant.Constants;
import br.com.damasceno.agenda.database.AppDatabase;
import br.com.damasceno.agenda.fragment.ContactFragment;
import br.com.damasceno.agenda.fragment.EventFragment;
import br.com.damasceno.agenda.fragment.TaskFragment;
import br.com.damasceno.agenda.helper.GlideApp;
import br.com.damasceno.agenda.helper.VolleyResponseListener;
import br.com.damasceno.agenda.model.Task;
import br.com.damasceno.agenda.util.SharedPreferencesUtils;
import br.com.damasceno.agenda.util.VolleyUtils;
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

    HashMap<String, String> userProfile;
    private AppDatabase db;
    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        String userName = null;
        String userEmail = null;

        db = AppDatabase.getInstance(this);

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

        //initialize and create the image loader logic
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder, String tag) {

                GlideApp
                        .with(imageView.getContext())
                        .load(uri)
                        .placeholder(placeholder)
                        .into(imageView);
            }
        });

        // Drawer Items
        PrimaryDrawerItem itemLogout = new PrimaryDrawerItem()
                .withIdentifier(1)
                .withSelectable(false)
                .withIcon(R.drawable.ic_exit_gray_24dp)
                .withName(getString(R.string.str_logout));

        PrimaryDrawerItem itemSettings = new PrimaryDrawerItem()
                .withIdentifier(2)
                .withSelectable(false)
                .withIcon(R.drawable.ic_settings_gray_24dp)
                .withName(getString(R.string.str_settings));

        // Getting User Profile from SharedPreferences
        userProfile = SharedPreferencesUtils.getUserProfile(this.getApplicationContext());

        ProfileDrawerItem profileItem = new ProfileDrawerItem()
                .withName(userProfile.get(KEY_USER_NAME))
                .withEmail(userProfile.get(KEY_USER_EMAIL))
                .withIcon(userProfile.get(KEY_USER_PICTURE));


        // Account Header
        final AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(false)
                .withSavedInstance(savedInstanceState)
                .withThreeSmallProfileImages(true)
                .addProfiles(profileItem)
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
                .setPositiveButton(getString(R.string.str_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Remove User Profile
                        SharedPreferencesUtils.removeUserProfile(getApplicationContext());

                        // Remove User From Database
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                db = AppDatabase.getInstance(MainActivity.this);
                                db.userDAO().removeUserByEmail(userProfile.get(KEY_USER_EMAIL));
                            }
                        });

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
