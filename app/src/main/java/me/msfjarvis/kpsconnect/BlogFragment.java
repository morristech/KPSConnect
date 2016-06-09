package me.msfjarvis.kpsconnect;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.afollestad.materialdialogs.MaterialDialog;

import me.msfjarvis.kpsconnect.R;

public class BlogFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blog, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        WebView blogWebView = (WebView) view.findViewById(R.id.blogwebview);
        blogWebView.loadUrl("http://www.khaitanpublicschool.com/blog");
        blogWebView.setWebViewClient(new WebViewClient());
        new MaterialDialog.Builder(getContext())
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .progress(true, 0)
                .show();
        WebSettings webSettings = blogWebView.getSettings();
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(getContext());
        String is_js_enabled = pref.getString("is_js_enabled","n/a");
        if (is_js_enabled.equals("yes")){
            webSettings.setJavaScriptEnabled(true);
        }
        if (is_js_enabled.equals("no")){
            webSettings.setJavaScriptEnabled(false);
        }else{
            webSettings.setJavaScriptEnabled(true);
        }
        webSettings.setLoadsImagesAutomatically(true);


        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    }

}
