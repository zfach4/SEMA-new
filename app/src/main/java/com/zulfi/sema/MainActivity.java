package com.zulfi.sema;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.zulfi.sema.about.AboutFragment;
import com.zulfi.sema.home.HomeFragment;
import com.zulfi.sema.smart_energy.SmartEnergyFragment;
import com.zulfi.sema.smart_light.SmartLightFragment;

public class MainActivity extends AppCompatActivity {

    private final String FRAGMENT_CONTENT = "fragment_content";
    private int content;
    private SharedPreferences mPreferences;

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        setupActionBar();

        drawer = findViewById(R.id.drawer_layout);

        toggle = setupDrawerToggle();
        toggle.setDrawerIndicatorEnabled(true);
        drawer.addDrawerListener(toggle);

        NavigationView navigationView = findViewById(R.id.nav_view);
        setupDrawerContent(navigationView);

        if (savedInstanceState == null) {
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.main_fragment_container, new HomeFragment());
            tx.commit();
        } else {
            int contentId = savedInstanceState.getInt(FRAGMENT_CONTENT);
            setContentMain(contentId);
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    // untuk menghandle bar item ketika di-klik
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(FRAGMENT_CONTENT, content);
    }

    private void setupActionBar() {
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        // untuk handle drawer click event
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        setContentMain(menuItem.getItemId());
        menuItem.setChecked(true);
        drawer.closeDrawers();
    }

    @SuppressLint("NonConstantResourceId")
    private void setContentMain(int contentId) {
        Fragment fragment = null;
        Class fragmentClass = HomeFragment.class;
        switch (contentId) {
            case R.id.nav_home:
                fragmentClass = HomeFragment.class;
                setTitle(R.string.app_name);
                break;
            case R.id.nav_smart_energy:
                fragmentClass = SmartEnergyFragment.class;
                setTitle(R.string.menu_smart_energy);
                break;
            case R.id.nav_smart_light:
                fragmentClass = SmartLightFragment.class;
                setTitle(R.string.menu_smart_light);
                break;
            case R.id.nav_about:
                fragmentClass = AboutFragment.class;
                setTitle(R.string.menu_about);
                break;
            case R.id.nav_logout:
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_fragment_container, fragment).commit();

        content = contentId;
    }
}