package me.msfjarvis.kpsconnect;

import android.content.Context;
import android.content.DialogInterface;
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
        final Context context = this;
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            try {
                Thread.sleep(1000);
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
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                finish();
            }
        } else {
            new MaterialDialog.Builder(this)
                    .title(R.string.app_name)
                    .content(R.string.not_connected)
                    .positiveText("OK")
                    .dismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            finish();
                        }
                    })
                    .show();
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
                        //Log.i(); only used for debugging lags, has no real use for now
                        drawerLayout.closeDrawers();
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_main, new BlogFragment());
                        ft.commit();
                        MaterialDialog.Builder builder = new MaterialDialog.Builder(MainActivity.this)
                                .title(R.string.progress_dialog)
                                .content(R.string.please_wait)
                                .progress(true, 0);
                        MaterialDialog dialog = builder.build();
                        dialog.show();
                        try {
                            Thread.sleep(2500);
                            dialog.dismiss();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                    case R.id.app_feedback:
                        Intent feedbackIntent = new Intent("me.msfjarvis.kpsconnect.FEEDBACKACTIVITY");
                        try {
                            startActivity(feedbackIntent);
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this,R.string.oops,Toast.LENGTH_SHORT).show();
                        }
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.about_kpsconnect:
                        Intent aboutIntent = new Intent("me.msfjarvis.kpsconnect.ABOUTACTIVITY");
                        try {
                            startActivity(aboutIntent);
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this,R.string.oops,Toast.LENGTH_SHORT).show();
                        }
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.logout:
                        finish();

                }
                return true;
            }
        });
        View header = navigationView.getHeaderView(0);
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

}


