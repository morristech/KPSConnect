package me.msfjarvis.kpsconnect.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;

import me.msfjarvis.kpsconnect.utils.CreateCard;

import static me.msfjarvis.kpsconnect.fragments.FeedFragmentStorage.FeedType;
import static me.msfjarvis.kpsconnect.utils.CreateCard.dpToPx;

public class FeedFragment extends Fragment {

    private static Context currentContext;

    public static FeedFragment createInstance(Context context, ArrayList<String>... feeds) {
        currentContext = context;
        FeedFragmentStorage.setFeeds(feeds);
        return new FeedFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        ScrollView finalView = new ScrollView(currentContext);
        LinearLayout listView = new LinearLayout(currentContext);
        finalView.addView(listView);
        listView.setOrientation(LinearLayout.VERTICAL);
        listView.setDividerDrawable(new ColorDrawable(Color.TRANSPARENT));
        listView.setDividerPadding(dpToPx(currentContext, 12));
        for ( int i = 0; i < FeedFragmentStorage.getFeeds(FeedType.TITLES).length; i++ )
            listView.addView(
                    CreateCard.newCard(
                            currentContext, getActivity(),
                            dpToPx(currentContext, 5), dpToPx(currentContext, 6),
                            dpToPx(currentContext, 4), dpToPx(currentContext, 4),
                            "#fefefe",
                            FeedFragmentStorage.getFeeds(FeedType.TITLES)[i], 18, "#010101",
                            FeedFragmentStorage.getFeeds(FeedType.CATEGORIES)[i], 14, "#0f0f0f",
                            FeedFragmentStorage.getFeeds(FeedType.LINKS)[i]
                    )
            );

        return finalView;
    }

}
