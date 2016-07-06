package me.msfjarvis.kpsconnect;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;;
import android.widget.ImageView;
import android.widget.TextView;
//import com.squareup.picasso.Picasso;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.Request;

public class MainFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, parent, false);
    }
    public class stringLoader {
        OkHttpClient client = new OkHttpClient();

        String run(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ImageView homeImage = (ImageView) view.findViewById(R.id.homeImageView);
        TextView homeText = (TextView) view.findViewById(R.id.homeTextView);
        String imageUri = "https://github.com/MSF-Jarvis.png";
        /*Picasso
                .with(getContext())
                .load(imageUri)
                .into(homeImage);*/
        stringLoader newsString = new stringLoader();
        try{
            String response = newsString.run("https://raw.githubusercontent.com/MSF-Jarvis/local_manifests/cm-13.0/local_manifest.xml");
            homeText.setText(response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
