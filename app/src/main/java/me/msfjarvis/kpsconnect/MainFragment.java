package me.msfjarvis.kpsconnect;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class MainFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, parent, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        Button button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent introIntent = new Intent("me.msfjarvis.kpsconnect.MAININTROACTIVITY");
                startActivity(introIntent);
            }
        });
        ImageView imageView = (ImageView) view.findViewById(R.id.homeImageView);
        Picasso.with(getContext())
                .load("http://khaitanpublicschool.com/blog/wp-content/themes/khaitan/images/logo-blog.png")
                .into(imageView);
        TextView textView = (TextView) view.findViewById(R.id.homeTextView);
        textView.setText("flat");


    }
}
