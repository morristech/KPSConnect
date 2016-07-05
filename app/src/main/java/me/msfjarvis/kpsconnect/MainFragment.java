package me.msfjarvis.kpsconnect;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MainFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ImageView homeImage = (ImageView) view.findViewById(R.id.homeImageView);
        TextView homeText = (TextView) view.findViewById(R.id.homeTextView);
        String imageUri = "https://github.com/MSF-Jarvis.png";
        Picasso
                .with(getContext())
                .load(imageUri)
                .into(homeImage);
    }
}
