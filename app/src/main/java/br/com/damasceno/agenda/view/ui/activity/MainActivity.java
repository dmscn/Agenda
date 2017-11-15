package br.com.damasceno.agenda.view.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;
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

import br.com.damasceno.agenda.view.adapter.PageAdapter;
import br.com.damasceno.agenda.constant.Constants;
import br.com.damasceno.agenda.service.repository.database.AppDatabase;
import br.com.damasceno.agenda.view.ui.fragment.ContactFragment;
import br.com.damasceno.agenda.view.ui.fragment.EventFragment;
import br.com.damasceno.agenda.view.ui.fragment.TaskFragment;
import br.com.damasceno.agenda.helper.GlideApp;
import br.com.damasceno.agenda.util.SharedPreferencesUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements Constants, TaskFragment.OnFragmentInteractionListener, EventFragment.OnFragmentInteractionListener, ContactFragment.OnFragmentInteractionListener {

    @BindView(R.id.tabs)
    TabLayout mTabs;

    @BindView(R.id.pager)
    ViewPager mViewPager;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.fab_menu)
    FloatingActionMenu mFabMenu;

    @BindView(R.id.fab_task)
    FloatingActionButton mFabTask;

    @BindView(R.id.fab_event)
    FloatingActionButton mFabEvent;

    @BindView(R.id.fab_contact)
    FloatingActionButton mFabContact;

    HashMap<String, String> mUserProfile;
    private AppDatabase mDB;
    private AlertDialog mAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Defining IconicsLayoutInflater to enable automatic xml icons detection
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        String userName = null;
        String userEmail = null;

        mDB = AppDatabase.getInstance(this);

        /*
         *  TOOLBAR
         */

        mToolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(mToolbar);
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
                .withIcon(CommunityMaterial.Icon.cmd_logout_variant)
                .withName(getString(R.string.str_logout));

        PrimaryDrawerItem itemSettings = new PrimaryDrawerItem()
                .withIdentifier(2)
                .withSelectable(false)
                .withIcon(CommunityMaterial.Icon.cmd_settings)
                .withName(getString(R.string.str_settings));

        PrimaryDrawerItem itemOpenSource = new PrimaryDrawerItem()
                .withIdentifier(3)
                .withSelectable(false)
                .withIcon(CommunityMaterial.Icon.cmd_github_circle)
                .withName(getString(R.string.str_open_source));

        // Getting User Profile from SharedPreferences
        mUserProfile = SharedPreferencesUtils.getUserProfile(this.getApplicationContext());

        ProfileDrawerItem profileItem = new ProfileDrawerItem()
                .withName(mUserProfile.get(KEY_USER_NAME))
                .withEmail(mUserProfile.get(KEY_USER_EMAIL))
                .withIcon(mUserProfile.get(KEY_USER_PICTURE));


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
                .withToolbar(mToolbar)
                .withAccountHeader(accountHeader)
                .withActionBarDrawerToggleAnimated(true)
                .withSavedInstance(savedInstanceState)
                .addDrawerItems(
                        itemLogout,
                        itemSettings,
                        itemOpenSource
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

        // Adding mTabs to layout
        mTabs.addTab(mTabs.newTab().setText(FRAG_TASK_TITLE));
        mTabs.addTab(mTabs.newTab().setText(FRAG_EVENT_TITLE));
        mTabs.addTab(mTabs.newTab().setText(FRAG_CONTACT_TITLE));
        mTabs.setTabGravity(TabLayout.GRAVITY_FILL);

        // Setting up PageAdapter
        final PageAdapter adapter = new PageAdapter(getSupportFragmentManager(), mTabs.getTabCount());
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabs));

        // Layout Actions
        mTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
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

        mFabMenu.setClosedOnTouchOutside(true);

        // Setting Options Icon
        mFabTask.setImageDrawable(new IconicsDrawable(this, CommunityMaterial.Icon.cmd_clipboard_outline).color(getResources().getColor(R.color.colorWhite)).paddingDp(2));
        mFabEvent.setImageDrawable(new IconicsDrawable(this, CommunityMaterial.Icon.cmd_calendar).color(getResources().getColor(R.color.colorWhite)).paddingDp(2));
        mFabContact.setImageDrawable(new IconicsDrawable(this, CommunityMaterial.Icon.cmd_account).color(getResources().getColor(R.color.colorWhite)).paddingDp(3));

        // Add new Task
        mFabTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TaskActivity.class);
                startActivity(intent);
                mFabMenu.close(false);
            }
        });

        // Add new Event
        mFabEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mFabMenu.close(false);
            }
        });

        // Add new Contact
        mFabContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mFabMenu.close(false);
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
                                mDB.userDAO().removeUserByEmail(mUserProfile.get(KEY_USER_EMAIL));
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

        mAlert = builder.create();
        mAlert.show();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
