package me.msfjarvis.kpsconnect.activities;

import android.content.ActivityNotFoundException;
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
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setType("plain/text");
            sendIntent.setData(Uri.parse("app@khaitanpublicschool.com"));
            sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
            sendIntent.putExtra(Intent.EXTRA_EMAIL, R.string.email);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, subjectText);
            sendIntent.putExtra(Intent.EXTRA_TEXT, messageText);
            try {
                startActivity(sendIntent);
                finish();
            }catch (ActivityNotFoundException e){
                Intent sendMessageIntent = new Intent(Intent.ACTION_SEND);
                sendMessageIntent.setType("text/plain");
                sendMessageIntent.putExtra(Intent.EXTRA_EMAIL, R.string.email);
                sendMessageIntent.putExtra(Intent.EXTRA_SUBJECT, subjectText);
                sendMessageIntent.putExtra(Intent.EXTRA_TEXT, messageText);
                if (sendMessageIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(sendMessageIntent);
                    finish();
                }else{
                    Snackbar.make(v,"No email app found! Can't send feedback!",Snackbar.LENGTH_LONG).show();
                }
            }catch (Exception e){
                Snackbar.make(v,"Tough luck mate, email couldn't be sent :(",Snackbar.LENGTH_LONG).show();
            }
        }

    }
}