package me.msfjarvis.kpsconnect;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.mikepenz.aboutlibraries.*;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import me.msfjarvis.apprate.AppRate;
import me.pushy.sdk.Pushy;
import me.pushy.sdk.exceptions.PushyException;
import com.mikepenz.aboutlibraries.ui.LibsFragment;
import com.mikepenz.aboutlibraries.ui.LibsSupportFragment;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    public NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Pushy.listen(this);
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
                    Intent introIntent = new Intent("me.msfjarvis.kpsconnect.MAININTROACTIVITY");
                    startActivity(introIntent);
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("is_first_run", "one");
                    edit.apply();
                } else if(is_first_run.equals("one")) {
                    new RegisterForPushNotificationsAsync().execute();
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("is_first_run","two");
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

    }
    public void onHome(){
        drawerLayout.closeDrawers();
        FragmentTransaction ht = getSupportFragmentManager().beginTransaction();
        ht.replace(R.id.content_main, new MainFragment());
        ht.commit();
    }
    public void onBlog(){
        drawerLayout.closeDrawers();
        Toast.makeText(getApplicationContext(),"Loading the blog may take some time depending on your connection",Toast.LENGTH_LONG).show();
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
    public void onAbout(){
        LibsSupportFragment fragment = new LibsBuilder()
                .supportFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, fragment);
        ft.commit();
    }

    String selected = "";
    public void initNavigationDrawer() {
        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        assert navigationView != null;
        selected = "home";
        onHome();
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
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.about_kpsconnect:
                        onAbout();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.logout:
                        finish();

                }
                return true;
            }
        });

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
    private class RegisterForPushNotificationsAsync extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... params)
        {
            String result;
           try
            {
                result = Pushy.register(MainActivity.this);
            }
            catch (PushyException exc)
            {
                result = exc.getMessage();
            }
            Log.d("Pushy", "Registration result: " + result);


            return result;
        }

        @Override
        protected void onPostExecute(String result)
        {
            // Activity died?
            if ( isFinishing() )
            {
                return;
            }
        }
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


