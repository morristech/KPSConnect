package me.msfjarvis.kpsconnect.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;

import me.msfjarvis.kpsconnect.R;

public class FeedActivity extends AppCompatActivity {
    public String title,category,content,featuredImage;
    public ImageView featuredImageView;
    public TextView titleTextView,categoryTextView,contentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_activity);
        try{
            featuredImageView = (ImageView) findViewById(R.id.feedImageView);
            titleTextView = (TextView) findViewById(R.id.feedTitleView);
            categoryTextView = (TextView) findViewById(R.id.feedCategoryView);
            contentTextView = (TextView) findViewById(R.id.feedContentView);
        }catch(NullPointerException exc){
            Log.d(getString(R.string.log_tag_feedview),exc.toString());
            Toast.makeText(getApplicationContext(),exc.toString(),Toast.LENGTH_SHORT).show();
            finish();
        }
        if (getIntent()!=null){
            Intent feedIntent = getIntent();
            try{
                title = feedIntent.getStringExtra("title");
                category = feedIntent.getStringExtra("category");
                content = feedIntent.getStringExtra("content");
                featuredImage = feedIntent.getStringExtra("featuredImage");
            }catch (NullPointerException exc) {
                Log.d(getString(R.string.log_tag_feedactivity), exc.toString());
            }
            titleTextView.setText(title);
            categoryTextView.setText(category);
            contentTextView.setText(Jsoup.parse(content).text());
            Picasso.get().load(featuredImage).into(featuredImageView);
        }
        else{
            Toast.makeText(getApplicationContext(),R.string.feed_null_intent_error_text,Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
