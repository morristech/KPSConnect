package me.msfjarvis.kpsconnect.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.xdevs23.net.DownloadUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SOTDFragment extends Fragment {
    public String IMAGE_URL="http://kpsconnect.msfjarvis.me:2015/sotd/image.jpeg";
    public String DESC_URL = "http://kpsconnect.msfjarvis.me:2015/sotd/desc.txt";
    public Bitmap currentBitmap;
    public String currentString;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout layout = new LinearLayout(getContext());
        final ImageView sotdImageView = new ImageView(getContext());
        final TextView sotdTextView = new TextView(getContext());
        final Handler handler = new Handler();
        final Runnable setImageRunnable = new Runnable() {
            @Override
            public void run() {
                sotdImageView.setImageBitmap(currentBitmap);
            }
        };
        final Runnable setDescRunnable = new Runnable() {
            @Override
            public void run() {
                sotdTextView.setText(currentString);
            }
        };
        Runnable loadImagesRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream inputStream = DownloadUtils.getInputStreamForConnection(
                            IMAGE_URL
                    );
                    currentBitmap = BitmapFactory.decodeStream(inputStream);
                    handler.post(setImageRunnable);
                } catch(Exception ex) {
                    ex.printStackTrace();
                    Log.d("KPSConnect", "Failed load of image " +
                            IMAGE_URL);
                }
            }
        };
        Runnable loadDescriptionRunnable = new Runnable() {
            @Override
            public void run() {
                try{
                    InputStream inputStream = DownloadUtils.getInputStreamForConnection(
                            DESC_URL
                    );
                    BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = r.readLine()) != null) {
                        currentString.concat(line);
                    }
                    handler.post(setDescRunnable);
                }catch (Exception exc){
                    Log.d("KPSConnect", "Failed load of desc " +
                            DESC_URL);
                }
            }
        };
        Thread loadImagesThread = new Thread(loadImagesRunnable);
        loadImagesThread.start();
        Thread loadDescriptionThread = new Thread(loadDescriptionRunnable);
        loadDescriptionThread.start();
        layout.addView(sotdImageView);
        layout.addView(sotdTextView);
        return layout;
    }
}