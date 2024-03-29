package me.msfjarvis.kpsconnect;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.ui.LibsSupportFragment;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import me.msfjarvis.kpsconnect.activities.MainIntroActivity;
import me.msfjarvis.kpsconnect.fragments.EOTDFragment;
import me.msfjarvis.kpsconnect.fragments.FeedFragment;
import me.msfjarvis.kpsconnect.fragments.SOTDFragment;
import me.msfjarvis.kpsconnect.rssmanager.OnRssLoadListener;
import me.msfjarvis.kpsconnect.rssmanager.RssItem;
import me.msfjarvis.kpsconnect.rssmanager.RssReader;
import me.msfjarvis.kpsconnect.utils.Utilities;
import me.msfjarvis.kpsconnect.utils.Constants;

public class MainActivity extends AppCompatActivity implements OnRssLoadListener {
    public static final String PREF_FIRST_RUN_KEY = "is_first_run";
    public static final String PREF_REGID_KEY = "token";
    public FeedFragment currentFeedFragmentInstance;
    private boolean isPaused = false;
    private boolean areFeedsLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = pref.edit();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHeader(R.layout.nav_header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.home).withIcon(R.drawable.ic_home_black_24dp).withIdentifier(1).withSelectable(true),
                        new PrimaryDrawerItem().withName(R.string.title_activity_about).withIcon(R.drawable.ic_info_black_24dp).withIdentifier(2).withSelectable(true),
                        new PrimaryDrawerItem().withName(R.string.eotd).withIcon(R.drawable.ic_event_black_24dp).withIdentifier(3).withSelectable(true),
                        new PrimaryDrawerItem().withName(R.string.sotd).withIcon(R.drawable.ic_school_black_24dp).withIdentifier(4).withSelectable(true),
                        new PrimaryDrawerItem().withName(R.string.title_activity_feedback).withIcon(R.drawable.ic_feedback_black_24dp).withIdentifier(5).withSelectable(false).withEnabled(true),
                        new PrimaryDrawerItem().withName(R.string.our_launch_video).withIcon(R.drawable.ic_featured_video_black_24dp).withIdentifier(6).withSelectable(true),
                        new PrimaryDrawerItem().withName(R.string.exit).withIcon(R.drawable.ic_exit_to_app_black_24dp).withIdentifier(7).withSelectable(false)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, @NotNull IDrawerItem drawerItem) {
                        if (drawerItem.getIdentifier() == 1) {
                            onHome();
                        } else if (drawerItem.getIdentifier() == 2) {
                            onAbout();
                        } else if (drawerItem.getIdentifier() == 3) {
                            onEotd();
                        } else if (drawerItem.getIdentifier() == 4) {
                            onSotd();
                        } else if (drawerItem.getIdentifier() == 5) {
                            customTab(Constants.FEEDBACK_URL);
                        } else if (drawerItem.getIdentifier() == 6) {
                            customTab(Constants.YOUTUBE_URL);
                        } else if (drawerItem.getIdentifier() == 7) {
                            finish();
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();
        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
        if (Utilities.isOnline(this)) {
            Log.d(getString(R.string.log_tag), getString(R.string.log_tag_internet_connected));
            FirebaseMessaging.getInstance().subscribeToTopic("news");
            edit.putString(PREF_REGID_KEY, FirebaseInstanceId.getInstance().getToken());
            String is_first_run = pref.getString(PREF_FIRST_RUN_KEY, "yes");
            if (is_first_run.equals("yes")) {
                edit.putString(PREF_FIRST_RUN_KEY, "no");
                edit.apply();
                Intent introIntent = new Intent(this, MainIntroActivity.class);
                startActivity(introIntent);
            }
        } else {
            new MaterialDialog.Builder(this)
                    .title(R.string.app_name)
                    .content(R.string.not_connected)
                    .positiveText(R.string.positive_text)
                    .dismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if (currentFeedFragmentInstance==null){finish();}
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
        FragmentTransaction ht = getSupportFragmentManager().beginTransaction();
        try {
            ht.replace(R.id.content_main, (currentFeedFragmentInstance == null ? new Fragment()
                :  currentFeedFragmentInstance));
        } catch(Exception e) {
            ht.replace(R.id.content_main, new FeedFragment());
        }
        ht.commit();
        Log.d(getString(R.string.log_tag), String.format(getString(R.string.log_null_check_feed), currentFeedFragmentInstance == null));
        if (currentFeedFragmentInstance == null){
            if (Utilities.isOnline(this)) {
                loadFeeds(Constants.FEED_URL);
            }
        }
    }
    public void onSotd() {
        if(isPaused) return;
        FragmentTransaction ht = getSupportFragmentManager().beginTransaction();
        ht.replace(R.id.content_main, new SOTDFragment());
        ht.commit();
    }
    public void onEotd() {
        if(isPaused) return;
        FragmentTransaction ht = getSupportFragmentManager().beginTransaction();
        ht.replace(R.id.content_main, new EOTDFragment());
        ht.commit();
    }

    public void onAbout() {
        if(isPaused) return;
        LibsSupportFragment fragment = new LibsBuilder()
        		.withAboutAppName("KPS Connect")
                .withAboutIconShown(true)
                .withAboutVersionShown(true)
                .withAboutDescription(getString(R.string.about_kpsconnect_desc))
                .withAboutSpecial1(getString(R.string.about_kpsconnect_team_title))
                .withAboutSpecial1Description(getString(R.string.about_kpsconnect_team_desc))
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
    
    private void customTab(String urlToLoad){
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setShowTitle(true);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(urlToLoad));
    }

    public void onResume(){
        super.onResume();
        isPaused = false;
    }

    @Override
    public void onPause() {
        isPaused = true;
        super.onPause();
    }

}



