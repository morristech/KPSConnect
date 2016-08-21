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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import me.msfjarvis.apprate.AppRate;
import me.msfjarvis.kpsconnect.fragments.FeedFragment;
import me.msfjarvis.kpsconnect.rssmanager.OnRssLoadListener;
import me.msfjarvis.kpsconnect.rssmanager.RssItem;
import me.msfjarvis.kpsconnect.rssmanager.RssReader;
import me.msfjarvis.kpsconnect.utils.APIThread;
import me.pushy.sdk.Pushy;
import me.pushy.sdk.exceptions.PushyException;

public class MainActivity extends AppCompatActivity implements OnRssLoadListener {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    public NavigationView navigationView;
    public static final String BASE_URL = "https://api.msfjarvis.me/regids/register";
    public String result;

    private FeedFragment currentFeedFragmentInstance;


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
                new RegisterForPushNotificationsAsync().execute();
                SharedPreferences pref =
                        PreferenceManager.getDefaultSharedPreferences(this);
                String is_first_run = pref.getString("is_first_run","n/a");
                if (is_first_run.equals("n/a")) {
                    Intent introIntent = new Intent("me.msfjarvis.kpsconnect.MAININTROACTIVITY");
                    startActivity(introIntent);
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("is_first_run", "one");
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
        loadFeeds("http://khaitanpublicschool.com/blog/feed/");

    }

    public void onHome() {
        drawerLayout.closeDrawers();
        FragmentTransaction ht = getSupportFragmentManager().beginTransaction();
        ht.replace(R.id.content_main, (currentFeedFragmentInstance == null ? new Fragment()
                                    :  currentFeedFragmentInstance));
        ht.commit();
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
        new LibsBuilder()
            .withActivityStyle(Libs.ActivityStyle.LIGHT)
            .start(MainActivity.this);
    }

    //load feeds
    private void loadFeeds(String url) {
        String[] urlArr = {url};

        new RssReader(MainActivity.this)
                .showDialog(true)
                .urls(urlArr)
                .parse(this);
    }

    @Override
    public void onSuccess(List<RssItem> rssItems) {
        final ArrayList<String> rssTitles = new ArrayList<>();
        final ArrayList<String> rssCategories = new ArrayList<>();
        final ArrayList<String> rssLinks = new ArrayList<>();
        final ArrayList<String> rssImages = new ArrayList<>();
        for (RssItem rssItem : rssItems) {
            rssTitles.add(rssItem.getTitle());
            rssCategories.add(rssItem.getCategory());
            rssLinks.add(rssItem.getLink());
            rssImages.add(rssItem.getImageUrl());
        }
        //noinspection unchecked
        currentFeedFragmentInstance = FeedFragment.createInstance(
                getApplicationContext(),
                rssTitles, rssCategories, rssLinks, rssImages
        );
        onHome();
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
            try {
                result = Pushy.register(MainActivity.this);
                SharedPreferences pref =
                        PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("regID",result);
                editor.apply();

            }catch (PushyException exc){
                exc.printStackTrace();
            }
            JSONObject postContent  = new JSONObject();
            try{
                postContent.put("id",result);
            }catch (JSONException exc){
                Toast.makeText(MainActivity.this,exc.toString(),Toast.LENGTH_LONG).show();
            }
            APIThread apiThread = new APIThread();
            apiThread.run(BASE_URL,postContent);
            return result;
        }

        @Override
        protected void onPostExecute(String result)
        {

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
        }
    }

}



