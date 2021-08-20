package com.zulfi.sema;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.firebase.client.Firebase;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    public NavigationView navigationView;

    private TextView tvUsername;

    private Firebase mRef;

    private FirebaseAuth mAuth;

    private DatabaseReference userRef;
    private TextView tvName, tvTelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //menyimpan kondisi logout
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        Toolbar toolbar = findViewById(R.id.toolbar_main);
        //membuat toolbar bisa menjadi activity window (bisa dikasih menu, dipencet, dll)
        setSupportActionBar(toolbar);

        //setting tampilan toolbar
        setupActionBar();

        // menu layout navigasi : muncul ketika burger icon ditekan
        drawer = findViewById(R.id.drawer_layout);

        tvUsername = findViewById(R.id.tv_name);

        //membuat komponen menu list ditampilkan ketika burger icon ditekan
        toggle = setupDrawerToggle();

        //jika true -> menampilkan burger icon, jika false -> back icon
        toggle.setDrawerIndicatorEnabled(true);
        //drawer menjadikan toggle sebagai listener (komponen yang akan bereaksi)
        drawer.addDrawerListener(toggle);

        //menu yang menampilkan list pada drawer : home, smart energy, dll
        navigationView = findViewById(R.id.nav_view);
        //mengisi profil username dll serta aksi ketika menuItem dipilih
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

    private void settingUsersData(){
        String UID = mPreferences.getString(getString(R.string.pref_uid_key), "-");
        if(UID != "-"){
            userRef = FirebaseDatabase.getInstance().getReference("Users");
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    // parsing nilai nama dari firebase ke tipe data String
//                    Users usersObject = snapshot.getValue(Users.class);
//                    // kasih nilai nama ke nama untuk dikirim ke SmartEnergyFragment
//                    if (usersObject != null){
//                        tvName.setText(usersObject.getNama());
//                        tvTelp.setText(usersObject.getTelp());
//                    } else {
//                        tvName.setText("-");
//                        tvTelp.setText("-");
//                    }

                    if (snapshot.hasChild(UID)){
                        DataSnapshot child = snapshot.child(UID);
                        //parsing data Users yang memiliki nilai UID menjadi objectUsers
                        Users usersObject = child.getValue(Users.class);
                        //setting text nama dan no telp sesui dengan field pada usersObject
                        tvName.setText(usersObject.getName());
                        tvTelp.setText(usersObject.getTelp());
                    } else {
                        tvName.setText("-");
                        tvTelp.setText("-");
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
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
        //menampilkan burger icon (memberikan tempat untuk suatu icon muncul : default back icon)
        actionbar.setDisplayHomeAsUpEnabled(true);
//        actionbar.setHomeButtonEnabled(true);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        // set user profile
        String imgUrl = mPreferences.getString(getString(R.string.pref_user_image_url), "-");
        String username = mPreferences.getString(getString(R.string.pref_user_fullname_key), "-");
        String email = mPreferences.getString(getString(R.string.pref_user_email_key), "-");

        View headerView = navigationView.getHeaderView(0);
        ImageView imageView = (ImageView) headerView.findViewById(R.id.img_profile_photo);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_account_circle));

        tvName = (TextView) headerView.findViewById(R.id.tv_name);
        tvTelp = (TextView) headerView.findViewById(R.id.tv_telp);

        TextView tvEmail = (TextView) headerView.findViewById(R.id.tv_email);
        tvEmail.setText(email);
        settingUsersData();
        // membuat listener untuk menu list (home, smart energy, dll)
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            //menentukan aksi yang akan dilakukan ketika salah satu menu item dipilih / ditekan
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });

        navigationView.setCheckedItem(R.id.nav_home);
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // melakukan aksi sesuai dengan menu item yang dipilih (menampilkan fragmen tertentu atau logout)
        setContentMain(menuItem.getItemId());
        // menandai bahwa menu telah dipilih dicodingan (bukan pa UI)
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
                logoutUser();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
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

    private void logoutUser() {

            mAuth.getInstance().signOut();

        // hapus data-data user dan status login jadi false

        // set isLogin = false
        mPreferences
                .edit()
                .putBoolean(getString(R.string.pref_is_login_key), false)
                .apply();
        // hapus user's fullname
        mPreferences
                .edit()
                .remove(getString(R.string.pref_user_fullname_key))
                .apply();
        // hapus user's profile photo
        mPreferences
                .edit()
                .remove(getString(R.string.pref_user_image_url))
                .apply();
        // hapus user's email
        mPreferences
                .edit()
                .remove(getString(R.string.pref_user_email_key))
                .apply();
    }

//    private void loadFirebase() {
//        mRef.addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                nilai = dataSnapshot.getValue(String.class);
//                tvString.setText(nilai);
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
//    }
}