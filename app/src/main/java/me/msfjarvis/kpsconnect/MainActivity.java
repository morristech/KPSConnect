package me.msfjarvis.kpsconnect;

import android.annotation.SuppressLint;
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
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.ui.LibsSupportFragment;

import org.xdevs23.ui.utils.BarColors;

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
    public static final String PREF_FIRST_RUN_KEY = "is_first_run";
    public static final String PREF_EMAIL_KEY = "email";
    public static final String PREF_REGID_KEY = "regID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Pushy.listen(this);
        setContentView(R.layout.activity_main);
        BarColors.setStatusBarColor(R.color.colorPrimaryDark,getWindow());
        BarColors.setNavigationBarColor(R.color.colorPrimaryDark,getWindow());
        final Context context = this;
        initNavigationDrawer();
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            Log.d("KPSConnect", "Internet is connected!");
            pref = PreferenceManager.getDefaultSharedPreferences(this);
            String is_first_run = pref.getString(PREF_FIRST_RUN_KEY,"yes");
            if (is_first_run.equals("yes")) {
                edit = pref.edit();
                edit.putString(PREF_FIRST_RUN_KEY,"no");
                edit.apply();
                Intent introIntent = new Intent(this, MainIntroActivity.class);
                startActivity(introIntent);
            }else {
                new RegisterForPushNotificationsAsync().execute();
            }
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            toolbar.inflateMenu(R.menu.menu_main);
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
        try {
            ht.replace(R.id.content_main, (currentFeedFragmentInstance == null ? new Fragment()
                :  currentFeedFragmentInstance));
        } catch(Exception e) {
            ht.replace(R.id.content_main, (new FeedFragment);
        }
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
    		    .withAboutSpecial2Description(String.format(getString(R.string.about_kpsconnect_id_desc), pref.getString("regID", "null")))
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

    private void ContactMe() {
        String[] TO = {getString(R.string.email_msfjarvis)};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(android.net.Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_about_kps_connect));

        try {
            startActivity(Intent.createChooser(emailIntent, getString(R.string.choose_email_app)));
        } catch (android.content.ActivityNotFoundException ex) {
            new BottomDialog.Builder(this)
                    .setTitle(getString(R.string.no_email_app_found_title))
                    .setContent(getString(R.string.no_email_app_found_message))
                    .setPositiveText(getString(R.string.download_gmail))
                    .setNegativeText(getString(R.string.ok))
                    .setCancelable(false)
                    .onPositive(new BottomDialog.ButtonCallback() {
                        @Override
                        public void onClick(@NonNull BottomDialog dialog) {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(android.net.Uri.parse(getResources().getString(R.string.gmail_link)));
                            startActivity(i);
                        }
                    })
                    .onNegative(new BottomDialog.ButtonCallback() {
                        @Override
                        public void onClick(@NonNull BottomDialog dialog) {
                            dialog.dismiss();
                        }
                    }).show();
        }
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
                        ContactMe();
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
                EasyIdMod easyIdMod = new EasyIdMod(getApplicationContext());
                String[] emailIds = easyIdMod.getAccounts();
                String emailString = "";
                if (emailIds != null && emailIds.length > 0) {
                    emailString=emailIds[0];
                    if (!(emailString == null)){
                        edit.putString(PREF_EMAIL_KEY,emailString);
                        edit.apply();
                    }
                }
                result = Pushy.register(MainActivity.this);
                edit.putString(PREF_REGID_KEY,result);
                edit.apply();
                Bridge
                        .post(BASE_URL)
                        .header("regID",result)
                        .header("email", emailString != null ? emailString : "null")
                        .request();
            }catch (PushyException exc){
                Log.d(getString(R.string.log_tag_pushy),Arrays.toString(exc.getStackTrace()));
            }catch (BridgeException exc){
                Log.d(getString(R.string.log_tag_bridge),Arrays.toString(exc.getStackTrace()));
            }
            catch (NullPointerException exc){
                Log.d(getString(R.string.log_tag_npe), Arrays.toString(exc.getStackTrace()));
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



