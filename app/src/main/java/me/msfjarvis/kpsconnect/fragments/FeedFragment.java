package me.msfjarvis.kpsconnect.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import org.xdevs23.net.DownloadUtils;
import java.io.InputStream;
import java.util.ArrayList;
import me.msfjarvis.kpsconnect.utils.CreateCard;
import static me.msfjarvis.kpsconnect.fragments.FeedFragmentStorage.FeedType;
import static me.msfjarvis.kpsconnect.utils.CreateCard.dpToPx;

public class FeedFragment extends Fragment {

    private static Context currentContext;

    private int currentI = 0;
    private Bitmap currentBitmap;
    private Handler handler;

    @SafeVarargs
    public static FeedFragment createInstance(Context context, ArrayList<String>... feeds) {
        currentContext = context;
        FeedFragmentStorage.setFeeds(feeds);
        return new FeedFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        handler = new Handler();
        ScrollView finalView = new ScrollView(currentContext);
        LinearLayout listView = new LinearLayout(currentContext);
        finalView.addView(listView);
        listView.setOrientation(LinearLayout.VERTICAL);
        listView.setDividerDrawable(new ColorDrawable(Color.TRANSPARENT));
        listView.setDividerPadding(dpToPx(currentContext, 12));
        final ImageView cardImages[] = new ImageView[FeedFragmentStorage.getFeeds(FeedType.TITLES).length];
        for ( int i = 0; i < cardImages.length; i++ )
            cardImages[i] = new ImageView(currentContext);
        for ( int i = 0; i < FeedFragmentStorage.getFeeds(FeedType.TITLES).length; i++ )
            listView.addView(
                    CreateCard.newCard(
                            currentContext, getActivity(),
                            dpToPx(currentContext, 5), dpToPx(currentContext, 6),
                            dpToPx(currentContext, 4), dpToPx(currentContext, 4),
                            "#fefefe",
                            FeedFragmentStorage.getFeeds(FeedType.TITLES)[i], 18, "#010101",
                            FeedFragmentStorage.getFeeds(FeedType.CATEGORIES)[i], 14, "#808080",
                            FeedFragmentStorage.getFeeds(FeedType.LINKS)[i], cardImages[i]
                    )
            );
        final Runnable setImageRunnable = new Runnable() {
            @Override
            public void run() {
                cardImages[currentI].setImageBitmap(currentBitmap);
            }
        };
        Runnable loadImagesRunnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < FeedFragmentStorage.getFeeds(FeedType.IMAGES).length; i++) {
                    try {
                        InputStream inputStream = DownloadUtils.getInputStreamForConnection(
                                FeedFragmentStorage.getFeeds(FeedType.IMAGES)[i]
                        );
                        currentI = i;
                        currentBitmap = BitmapFactory.decodeStream(inputStream);
                        handler.post(setImageRunnable);
                    } catch(Exception ex) {
                        ex.printStackTrace();
                        Log.d("KPSConnect", "Failed load of image " +
                            FeedFragmentStorage.getFeeds(FeedType.IMAGES)[i]);
                    }
                }
            }
        };
        Thread loadImagesThread = new Thread(loadImagesRunnable);
        loadImagesThread.start();
        return finalView;
    }

}
