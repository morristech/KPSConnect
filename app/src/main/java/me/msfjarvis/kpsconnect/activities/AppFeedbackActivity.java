package me.msfjarvis.kpsconnect.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import me.msfjarvis.kpsconnect.R;


public class AppFeedbackActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarFeedback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void sendMessage(View v) {
        EditText subject = (EditText) findViewById(R.id.subject);
        EditText message = (EditText) findViewById(R.id.message);
        String subjectText = subject.getText().toString();
        String messageText = message.getText().toString();
        if (subjectText.equals("")) {
            Snackbar.make(v, "No subject line!", Snackbar.LENGTH_SHORT).show();
        } else if (messageText.equals("")) {
            Snackbar.make(v, "No message body!", Snackbar.LENGTH_SHORT).show();
        } else {
            Uri uri = Uri.parse("mailto:" + getResources().getString(R.string.email))
                    .buildUpon()
                    .appendQueryParameter("subject", subjectText)
                    .appendQueryParameter("body", messageText)
                    .build();
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
            startActivity(Intent.createChooser(emailIntent, "Choose your email app"));
        }

    }
}