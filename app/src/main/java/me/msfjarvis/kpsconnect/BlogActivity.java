package me.msfjarvis.kpsconnect;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

public class BlogActivity extends Activity {
    String blogURL;
    WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        if (getIntent().getStringExtra("url")!=null){
            blogURL = getIntent().getStringExtra("url");
        }else{
            Toast.makeText(this,"no blogurl provided!",Toast.LENGTH_SHORT).show();
        }
        webView = (WebView) findViewById(R.id.blogwebview);
        webView.loadUrl(blogURL);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getKeyCode()==KeyEvent.KEYCODE_BACK){
                    if (webView.canGoBack()){
                        webView.goBack();
                    }else{
                        finish();
                    }
                }
                return false;
            }
        });
        final FloatingActionButton shareFab = (FloatingActionButton) findViewById(R.id.shareFab);
                shareFab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                String shareUrl = webView.getUrl();
                                String shareTitle = webView.getTitle();
                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setType("text/plain");
                                String finalShareString = shareTitle + " - " + shareUrl;
                                shareIntent.putExtra(Intent.EXTRA_TEXT, finalShareString);
                                try{
                                        startActivity(Intent.createChooser(shareIntent, "Share link using"));
                                    }catch (Exception e){
                                        Snackbar.make(view,"No share action provider found!",Snackbar.LENGTH_SHORT).show();
                                    }
                            }
                    });




    }
}
