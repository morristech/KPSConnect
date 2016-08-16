package me.msfjarvis.kpsconnect.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.xdevs23.ui.view.listview.XDListView;

import java.util.ArrayList;

import static me.msfjarvis.kpsconnect.fragments.FeedFragmentStorage.FeedType;

public class FeedFragment extends Fragment {

    private static Context currentContext;

    public static FeedFragment createInstance(Context context, ArrayList<String>... feeds) {
        currentContext = context;
        FeedFragmentStorage.setFeeds(feeds);
        return new FeedFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        ListView listView = new ListView(currentContext);
        ListView.LayoutParams lp = new ListView.LayoutParams(
                ListView.LayoutParams.MATCH_PARENT,
                ListView.LayoutParams.MATCH_PARENT
        );
        listView.setLayoutParams(lp);
        listView.setAdapter(XDListView.createDual(currentContext,
                FeedFragmentStorage.getFeeds(FeedType.TITLES),
                FeedFragmentStorage.getFeeds(FeedType.CATEGORIES)));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(FeedFragmentStorage.getFeeds(FeedType.LINKS)[position])));
            }
        });
        return listView;
    }

}
