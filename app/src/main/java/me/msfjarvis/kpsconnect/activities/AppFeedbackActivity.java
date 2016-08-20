package me.msfjarvis.kpsconnect.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import me.msfjarvis.kpsconnect.R;
import me.msfjarvis.kpsconnect.utils.APIThread;


public class AppFeedbackActivity extends AppCompatActivity {

    String emailText,subjectText,messageText;
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
        EditText email = (EditText) findViewById(R.id.email);
        EditText subject = (EditText) findViewById(R.id.subject);
        EditText message = (EditText) findViewById(R.id.message);
        emailText = email.getText().toString();
        subjectText = subject.getText().toString();
        messageText = message.getText().toString();
        new sendFeedback().execute();
    }
    private class sendFeedback extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... params){
            if(emailText!="" || subjectText!="" || messageText!="") {
                JSONObject feedback = new JSONObject();
                try {
                    feedback.put("email",emailText);
                    feedback.put("subject",subjectText);
                    feedback.put("message",messageText);
                } catch (JSONException exc) {
                    Toast.makeText(getApplicationContext(), exc.toString(), Toast.LENGTH_SHORT).show();
                }
                new APIThread().sendFeedback(feedback);
            }else{
                if (emailText.equals("")){
                    Toast.makeText(getApplicationContext(), "Enter email!", Toast.LENGTH_SHORT).show();
                }
                else if (subjectText.equals("")){
                    Toast.makeText(getApplicationContext(), "Enter subject!", Toast.LENGTH_SHORT).show();
                }
                else if (messageText.equals("")){
                    Toast.makeText(getApplicationContext(), "Enter message!", Toast.LENGTH_SHORT).show();
                }
            }
            return "execution complete";
        }
    }
}