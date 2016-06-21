package me.msfjarvis.kpsconnect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void showMore(View v){
        new MaterialDialog.Builder(AboutActivity.this)
                .title("Additional Contributors")
                .customView(R.layout.contributors, true)
                .show();


    };

}
