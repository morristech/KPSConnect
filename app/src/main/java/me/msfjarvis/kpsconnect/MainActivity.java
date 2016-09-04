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
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.bridge.Bridge;
import com.afollestad.bridge.BridgeException;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.ui.LibsSupportFragment;

import org.xdevs23.ui.utils.BarColors;

import java.util.ArrayList;
import java.util.List;

import me.msfjarvis.kpsconnect.fragments.EOTDFragment;
import me.msfjarvis.kpsconnect.fragments.FeedFragment;
import me.msfjarvis.kpsconnect.fragments.SOTDFragment;
import me.msfjarvis.kpsconnect.rssmanager.OnRssLoadListener;
import me.msfjarvis.kpsconnect.rssmanager.RssItem;
import me.msfjarvis.kpsconnect.rssmanager.RssReader;

import me.pushy.sdk.Pushy;
import me.pushy.sdk.exceptions.PushyException;

public class MainActivity extends AppCompatActivity implements OnRssLoadListener {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    public NavigationView navigationView;
    public static final String BASE_URL = "http://api.msfjarvis.me:2015/regids/register";
    public static final String FEED_URL = "http://khaitanpublicschool.com/blog/feed/";
    public String result;
    public FeedFragment currentFeedFragmentInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Pushy.listen(this);
        setContentView(R.layout.activity_main);
        BarColors.setStatusBarColor(R.color.colorPrimaryDark,getWindow());
        BarColors.setNavigationBarColor(R.color.colorPrimaryDark,getWindow());
        final Context context = this;
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            new RegisterForPushNotificationsAsync().execute();
            SharedPreferences pref =
                    PreferenceManager.getDefaultSharedPreferences(this);
            String is_first_run = pref.getString("is_first_run","n/a");
            if (is_first_run.equals("n/a")) {
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("is_first_run", "one");
                edit.apply();
                Intent introIntent = new Intent("me.msfjarvis.kpsconnect.MAININTROACTIVITY");
                startActivity(introIntent);
            }
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            toolbar.inflateMenu(R.menu.menu_main);
            initNavigationDrawer();
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
    }

    public void onSotd(){
        drawerLayout.closeDrawers();
        FragmentTransaction ht = getSupportFragmentManager().beginTransaction();
        ht.replace(R.id.content_main, new SOTDFragment());
        ht.commit();
    }
    public void onEotd(){
        drawerLayout.closeDrawers();
        FragmentTransaction ht = getSupportFragmentManager().beginTransaction();
        ht.replace(R.id.content_main, new EOTDFragment());
        ht.commit();
    }

    public void onFeedback(){
        drawerLayout.closeDrawers();
        Intent feedbackIntent = new Intent("me.msfjarvis.kpsconnect.FEEDBACKACTIVITY");
        try {
            startActivity(feedbackIntent);
        }catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, R.string.oops, Toast.LENGTH_SHORT).show();
        }
    }
    public void onAbout(){
        drawerLayout.closeDrawers();
        LibsSupportFragment fragment = new LibsBuilder()
                .supportFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, fragment);
        ft.commit();
    }

    //load feeds
    private void loadFeeds(String url, boolean showDialog) {
        String[] urlArr = {url};

        new RssReader(MainActivity.this)
                .showDialog(showDialog)
                .urls(urlArr)
                .parse(this);
    }

    @Override
    public void onSuccess(List<RssItem> rssItems) {
        final ArrayList<String> rssTitles = new ArrayList<>();
        final ArrayList<String> rssCategories = new ArrayList<>();
        final ArrayList<String> rssLinks = new ArrayList<>();
        final ArrayList<String> rssImages = new ArrayList<>();
        final ArrayList<String> rssContents = new ArrayList<>();
        for (RssItem rssItem : rssItems) {
            rssTitles.add(rssItem.getTitle());
            rssCategories.add(rssItem.getCategory());
            rssLinks.add(rssItem.getLink());
            rssImages.add(rssItem.getImageUrl());
            rssContents.add(rssItem.getDescription());
        }
        currentFeedFragmentInstance = FeedFragment.createInstance(
                getApplicationContext(),
                rssTitles, rssCategories, rssLinks, rssImages, rssContents
        );
        FragmentTransaction ht = getSupportFragmentManager().beginTransaction();
        ht.replace(R.id.content_main, (currentFeedFragmentInstance == null ? new Fragment()
                :  currentFeedFragmentInstance));
        ht.commit();
        if(currentFeedFragmentInstance == null){loadFeeds(FEED_URL,true);}
    }

    @Override
    public void onFailure(String message) {
        Toast.makeText(MainActivity.this, "Error:\n" + message, Toast.LENGTH_SHORT).show();
    }

    String selected = "";
    public void initNavigationDrawer() {
        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        assert navigationView != null;
        selected = "home";
        navigationView.setCheckedItem(R.id.home);
        loadFeeds(FEED_URL,true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id){
                    case R.id.home:
                        loadFeeds(FEED_URL,false);
                        selected = "home";
                        break;
                    case R.id.app_feedback:
                        onFeedback();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.about_kpsconnect:
                        selected = "about_kpsconnect";
                        onAbout();
                        break;
                    case R.id.sotd:
                        selected = "sotd";
                        onSotd();
                        break;
                    case R.id.eotd:
                        selected = "eotd";
                        onEotd();
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
            try {
                result = Pushy.register(MainActivity.this);
                SharedPreferences pref =
                        PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("regID",result);
                editor.apply();
                Bridge
                        .post(BASE_URL)
                        .header("regID",result)
                        .request();
            }catch (PushyException exc){
                Log.d("Pushy",exc.toString());
            }catch (BridgeException exc){
                Log.d("Bridge",exc.toString());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result)
        {
            Log.d("KPSConnect",result);
        }
    }
    public void onResume(){
        super.onResume();
        switch (selected){
            case "home":
                navigationView.setCheckedItem(R.id.home);
                break;
            case "about_kpsconnect":
                navigationView.setCheckedItem(R.id.about_kpsconnect);
                break;
            case "eotd":
                navigationView.setCheckedItem(R.id.eotd);
                break;
            case "sotd":
                navigationView.setCheckedItem(R.id.sotd);
                break;
        }
    }

}



