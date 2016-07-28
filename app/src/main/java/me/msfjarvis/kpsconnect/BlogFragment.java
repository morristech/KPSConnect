package me.msfjarvis.kpsconnect;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BlogFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blog, parent, false);
    }
    private WebView myWebView;
    private DrawerLayout drawerLayout;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        myWebView = (WebView) view.findViewById(R.id.blogwebview);
        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer);
        myWebView.getSettings().setLoadsImagesAutomatically(true);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        myWebView.setWebViewClient(new MyBrowser());
        Bundle bundle = this.getArguments();
        String url = "";
        if (bundle != null) {
            url = bundle.getString("url","http://www.khaitanpublicschool.com/blog");
        }
        myWebView.loadUrl(url);
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setSupportZoom(true);
        myWebView.getSettings().setBuiltInZoomControls(true); // allow pinch to zooom
        myWebView.getSettings().setDisplayZoomControls(false); // disable the default zoom controls on the page
        final FloatingActionButton shareFab = (FloatingActionButton) view.findViewById(R.id.shareFab);
        shareFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shareUrl = myWebView.getUrl();
                String shareTitle = myWebView.getTitle();
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
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (myWebView.canGoBack()) {
                        myWebView.goBack();
                    } else {
                        drawerLayout.openDrawer(Gravity.RIGHT,true);
                    }
                    return true;
            }

        }
        return onKeyDown(keyCode, event);
    }
}


