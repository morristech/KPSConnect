package me.msfjarvis.kpsconnect;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BlogFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blog, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        final WebView blogWebView = (WebView) view.findViewById(R.id.blogwebview);
        blogWebView.loadUrl("http://www.khaitanpublicschool.com/blog");
        blogWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = blogWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
        webSettings.setGeolocationEnabled(false);
    }

}
