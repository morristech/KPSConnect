package me.msfjarvis.kpsconnect.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import me.msfjarvis.kpsconnect.utils.Variables;

public class YouTubeShizActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        WebView webView = new WebView(context);
        webView.loadUrl(new Variables().getYouTubeURL());
        setContentView(webView);
    }
}