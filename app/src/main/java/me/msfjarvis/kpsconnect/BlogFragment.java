package me.msfjarvis.kpsconnect;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class BlogFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blog, parent, false);
    }
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        final WebView blogWebView = (WebView) view.findViewById(R.id.blogwebview);
        blogWebView.loadUrl("http://www.khaitanpublicschool.com/blog");
        WebSettings webSettings = blogWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
        webSettings.setGeolocationEnabled(false);
        final FloatingActionButton shareFab = (FloatingActionButton) view.findViewById(R.id.shareFab);
        shareFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shareUrl = blogWebView.getUrl();
                String shareTitle = blogWebView.getTitle();
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
