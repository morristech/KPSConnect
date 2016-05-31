package me.msfjarvis.kpsconnect;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import me.msfjarvis.kpsconnect.BlogFragment;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    public NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        initNavigationDrawer();

    }
    public void onHome(){
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
                        // Begin the transaction
                        drawerLayout.closeDrawers();
                        Toast.makeText(getApplicationContext(),"Loading the blog may take some time depending on your connection",Toast.LENGTH_LONG).show();
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_main, new BlogFragment());
                        ft.commit();
                        break;
                    case R.id.about_kpsconnect:
                        Intent myIntent = new Intent("me.msfjarvis.kpsconnect.ABOUTACTIVITY");
                        startActivity(myIntent);
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


