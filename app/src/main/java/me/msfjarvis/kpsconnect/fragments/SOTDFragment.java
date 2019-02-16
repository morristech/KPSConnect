package me.msfjarvis.kpsconnect.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baoyz.widget.PullRefreshLayout;
import com.squareup.picasso.Picasso;

import me.msfjarvis.kpsconnect.R;

public class SOTDFragment extends Fragment {
    public String IMAGE_URL="https://kpsconnect.msfjarvis.me/sotd/image.jpeg";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_template, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        final ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        //AppCompatTextView textView = (AppCompatTextView) view.findViewById(R.id.textView);
        Picasso.get().load(IMAGE_URL).into(imageView);
        final PullRefreshLayout pullRefreshLayout = (PullRefreshLayout) view.findViewById(R.id.pullRefreshLayout);
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Picasso.get().load(IMAGE_URL).into(imageView);
            }
        });

    }
}