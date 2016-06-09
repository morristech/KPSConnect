package me.msfjarvis.kpsconnect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    public NavigationView navigationView;
    private static final String TAG = "KPS Connect";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context = getApplicationContext();
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        Log.i(TAG, "Connection status received");
        if (isConnected) {
            try {
                Log.i(TAG, "Connection is a go!");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                finish();
            }
        } else {
            Log.i(TAG, "No bloody connection");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "Setting error text");
            Toast.makeText(context, "No connection. The app needs an active internet connection to function",Toast.LENGTH_LONG).show();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                Log.i(TAG, "Killing activity");
                finish();

            }
        }
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(this);
        String is_first_run = pref.getString("is_first_run","n/a");
        if (is_first_run.equals("n/a")) {
            new MaterialDialog.Builder(this)
                    .title(R.string.app_name)
                    .customView(R.layout.intro, false)
                    .positiveText("OK")
                    .show();
            SharedPreferences.Editor edit = pref.edit();
            edit.putString("is_first_run", "no");
            edit.apply();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        initNavigationDrawer();
        onHome();

    }
    public void onHome(){
        FragmentTransaction mainFragment = getSupportFragmentManager().beginTransaction();
        mainFragment.replace(R.id.content_main, new MainFragment());
        mainFragment.commit();
        drawerLayout.closeDrawers();
    }

    public void initNavigationDrawer() {

        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id){
                    case R.id.home:
                        onHome();
                        break;
                    case R.id.blog:
                        drawerLayout.closeDrawers();
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_main, new BlogFragment());
                        ft.commit();
                        break;
                    case R.id.about_kpsconnect:
                        Intent aboutIntent = new Intent("me.msfjarvis.kpsconnect.ABOUTACTIVITY");
                        startActivity(aboutIntent);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.settings:
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.logout:
                        Toast.makeText(getApplicationContext(),"Exiting...",Toast.LENGTH_SHORT).show();
                        finish();

                }
                return true;
            }
        });
        View header = navigationView.getHeaderView(0);
        TextView tv_email = (TextView)header.findViewById(R.id.tv_email);
        tv_email.setText(R.string.email);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


    }
    public void onResume(){
        super.onResume();
        navigationView.setCheckedItem(R.id.home);
        onHome();

    }

}


