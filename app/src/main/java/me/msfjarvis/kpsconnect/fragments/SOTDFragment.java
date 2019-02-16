package me.msfjarvis.kpsconnect.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baoyz.widget.PullRefreshLayout;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import me.msfjarvis.kpsconnect.R;

public class SOTDFragment extends Fragment {
    private String IMAGE_URL="https://kpsconnect.msfjarvis.me/sotd/image.jpeg";


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_template, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        final ImageView imageView = view.findViewById(R.id.imageView);
        //AppCompatTextView textView = (AppCompatTextView) view.findViewById(R.id.textView);
        Picasso.get().load(IMAGE_URL).into(imageView);
        final PullRefreshLayout pullRefreshLayout = view.findViewById(R.id.pullRefreshLayout);
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Picasso.get().load(IMAGE_URL).into(imageView);
            }
        });

    }
}