package me.msfjarvis.kpsconnect.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.squareup.picasso.Picasso;

import static me.msfjarvis.kpsconnect.utils.CreateCard.dpToPx;

public class EOTDFragment extends Fragment {
    public String IMAGE_URL = "http://kpsconnect.msfjarvis.me:2015/eotd/image.jpeg";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CardView imageCard = new CardView(getContext());
        RelativeLayout.LayoutParams cardParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        RelativeLayout.LayoutParams imgParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        int cm = dpToPx(getContext(), 16);
        cardParams.setMargins(cm, cm, cm, cm);
        imageCard.setLayoutParams(cardParams);
        imageCard.setContentPadding(8,8,8,8);
        imageCard.setRadius(8);
        imageCard.setCardElevation(4);
        final ImageView eotdImageView = new ImageView(getContext());
        eotdImageView.setLayoutParams(imgParams);
        eotdImageView.setPadding(16, 16, 16, 16);
        Picasso.with(getContext()).load(IMAGE_URL).into(eotdImageView);
        imageCard.addView(eotdImageView);
        return imageCard;
    }
}