package me.msfjarvis.kpsconnect;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.ui.LibsSupportFragment;

import org.xdevs23.ui.utils.BarColors;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import github.nisrulz.easydeviceinfo.base.EasyIdMod;
import me.msfjarvis.kpsconnect.activities.MainIntroActivity;
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
    public static final String BASE_URL = "https://api.msfjarvis.me/regids/register";
    public static final String FEED_URL = "http://khaitanpublicschool.com/blog/feed/";
    public String result;
    public String selected = "";
    public FeedFragment currentFeedFragmentInstance;
    private boolean isPaused = false;
    private boolean areFeedsLoading = false;
    private SharedPreferences pref;
    private SharedPreferences.Editor edit;
    CustomTabsClient mClient;
    public static final String PREF_FIRST_RUN_KEY = "is_first_run";
    public static final String PREF_EMAIL_KEY = "email";
    public static final String PREF_REGID_KEY = "regID";
    public static final String FEEDBACK_URL = "https://kpsconnect.msfjarvis.me/feedback.html";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Pushy.listen(this);
        setContentView(R.layout.activity_main);
        BarColors.setStatusBarColor(R.color.colorPrimaryDark,getWindow());
        BarColors.setNavigationBarColor(R.color.colorPrimaryDark,getWindow());
        final Context context = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        initNavigationDrawer();
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            Log.d(getString(R.string.log_tag), getString(R.string.log_tag_internet_connected));
            pref = PreferenceManager.getDefaultSharedPreferences(this);
            String is_first_run = pref.getString(PREF_FIRST_RUN_KEY,"yes");
            if (is_first_run.equals("yes")) {
                edit = pref.edit();
                edit.putString(PREF_FIRST_RUN_KEY,"no");
                edit.apply();
                Intent introIntent = new Intent(this, MainIntroActivity.class);
                startActivity(introIntent);
            }
            if (Pushy.isRegistered(getApplicationContext())){
                new RegisterForPushNotificationsAsync().execute();
            }
        } else {
            new MaterialDialog.Builder(this)
                    .title(R.string.app_name)
                    .content(R.string.not_connected)
                    .positiveText(R.string.positive_text)
                    .dismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            finish();
                        }
                    })
                    .show();
        }
        onHome();
    }

    @SuppressLint("StringFormatMatches")
    public void onHome() {
        if(isPaused) return;
        Log.d(getString(R.string.log_tag), getString(R.string.log_called_home));
        drawerLayout.closeDrawers();
        FragmentTransaction ht = getSupportFragmentManager().beginTransaction();
        ht.replace(R.id.content_main, (currentFeedFragmentInstance == null ? new Fragment()
                :  currentFeedFragmentInstance));
        ht.commit();
        Log.d(getString(R.string.log_tag), String.format(getString(R.string.log_null_check_feed), currentFeedFragmentInstance == null));
        if(currentFeedFragmentInstance == null) loadFeeds(FEED_URL);
    }
    public void onSotd() {
        if(isPaused) return;
        drawerLayout.closeDrawers();
        FragmentTransaction ht = getSupportFragmentManager().beginTransaction();
        ht.replace(R.id.content_main, new SOTDFragment());
        ht.commit();
    }
    public void onEotd() {
        if(isPaused) return;
        drawerLayout.closeDrawers();
        FragmentTransaction ht = getSupportFragmentManager().beginTransaction();
        ht.replace(R.id.content_main, new EOTDFragment());
        ht.commit();
    }

    public void onAbout() {
        if(isPaused) return;
        drawerLayout.closeDrawers();
        LibsSupportFragment fragment = new LibsBuilder()
        		.withAboutAppName("KPS Connect")
                .withAboutIconShown(true)
                .withAboutVersionShown(true)
                .withAboutDescription(getString(R.string.about_kpsconnect_desc))
                .withAboutSpecial1(getString(R.string.changelog_title))
                .withAboutSpecial1Description(getString(R.string.aboutLibraries_description_special1_text))
    		    .withAboutSpecial2(getString(R.string.about_title_id))
    		    .withAboutSpecial2Description(String.format(getString(R.string.about_kpsconnect_id_desc), pref.getString("regID", "unregistered")))
    		    .withAboutSpecial3(getString(R.string.about_kpsconnect_team_title))
    		    .withAboutSpecial3Description(getString(R.string.about_kpsconnect_team_desc))
                .supportFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, fragment);
        ft.commit();
    }


    //load feeds
    private void loadFeeds(String url) {
        if(areFeedsLoading) return;
        Log.d("KPSConnect", "Loading feeds...");
        areFeedsLoading = true;
        String[] urlArr = {url};

        new RssReader(MainActivity.this)
                .showDialog(true)
                .urls(urlArr)
                .parse(this);
    }

    @Override
    public void onSuccess(List<RssItem> rssItems) {
        if(isPaused) return;
        Log.d(getString(R.string.log_tag), getString(R.string.log_feeds_loaded));
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
        areFeedsLoading = false;
        Log.d(getString(R.string.log_tag), getString(R.string.log_calling_home));
        onHome();
    }

    @Override
    public void onFailure(String message) {
        Toast.makeText(MainActivity.this, String.format(getString(R.string.error_message), message), Toast.LENGTH_SHORT).show();
    }
    
    private void customTab(){
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(getResources().getColor(R.color.colorPrimaryDark));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(FEEDBACK_URL));
    }

    public void initNavigationDrawer() {
        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        assert navigationView != null;
        selected = "home";
        navigationView.setCheckedItem(R.id.home);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        onHome();
                        selected = "home";
                        break;
                    case R.id.app_feedback:
                        customTab();
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
                        break;
                    default:
                        break;
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
        onHome();
    }
    private class RegisterForPushNotificationsAsync extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... params)
        {
            try {
                Log.d(getString(R.string.log_tag),"Inside AsyncTask");
                EasyIdMod easyIdMod = new EasyIdMod(getApplicationContext());
                String emailId = easyIdMod.getAccounts()[0];
                result = Pushy.register(getApplicationContext());
                new URL(String.format(BASE_URL+"?email=%s&regID=%s", emailId,result)).openConnection();
                edit.putString(PREF_REGID_KEY,result);
                edit.putString(PREF_EMAIL_KEY,emailId);
                edit.apply();
            }catch (PushyException exc){
                Log.d(getString(R.string.log_tag_pushy),Arrays.toString(exc.getStackTrace()));
            } catch (NullPointerException exc){
                Log.d(getString(R.string.log_tag_npe), Arrays.toString(exc.getStackTrace()));
            } catch (IOException exc){
                Log.d(getString(R.string.api_call), Arrays.toString(exc.getStackTrace()));
            }
            return result;
        }
    }

    public void onResume(){
        super.onResume();
        isPaused = false;
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
            default:
                break;
        }
    }

    @Override
    public void onPause() {
        isPaused = true;
        super.onPause();
    }

}



