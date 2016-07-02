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
import com.tjeannin.apprate.AppRate;

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
        new AppRate(this)
                .setMinDaysUntilPrompt(3)
                .setMinLaunchesUntilPrompt(5)
                .setShowIfAppHasCrashed(false)
                .init();
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
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
    public void onBlog(){
        drawerLayout.closeDrawers();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, new BlogFragment());
        ft.commit();
    }

    public void onFeedback(){
        Intent feedbackIntent = new Intent("me.msfjarvis.kpsconnect.FEEDBACKACTIVITY");
        try {
            startActivity(feedbackIntent);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(MainActivity.this,R.string.oops,Toast.LENGTH_SHORT).show();
        }
        drawerLayout.closeDrawers();
    }
    String selected = "";
    public void initNavigationDrawer() {
        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        assert navigationView != null;
        selected = "home";
        navigationView.setCheckedItem(R.id.home);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id){
                    case R.id.home:
                        onHome();
                        selected = "home";
                        break;
                    case R.id.blog:
                        onBlog();
                        selected = "blog";
                        break;
                    case R.id.app_feedback:
                        onFeedback();
                        break;
                    case R.id.about_kpsconnect:
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.logout:
                        finish();

                }
                return true;
            }
        });
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
        if (selected.equals("home")){
            navigationView.setCheckedItem(R.id.home);
        }else if (selected.equals("blog")){
            navigationView.setCheckedItem(R.id.blog);
        }
    }

}


