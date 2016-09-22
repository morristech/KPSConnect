package me.msfjarvis.kpsconnect.activities;

import android.Manifest;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.app.OnNavigationBlockedListener;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;
import com.heinrichreimersoftware.materialintro.slide.Slide;

import me.msfjarvis.kpsconnect.R;

public class MainIntroActivity extends IntroActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addSlide(new SimpleSlide.Builder()
                .title(R.string.slide_1_title)
                .description(R.string.slide_1_description)
                .image(R.drawable.ic_launcher)
                .background(R.color.white)
                .build());
                
        addSlide(new SimpleSlide.Builder()
                .title(R.string.usage_title)
                .description(R.string.usage_desc)
                // TODO Add in necessary image
                .image(R.drawable.ic_launcher)
                .background(R.color.white)
                .build());
                
        final SimpleSlide permissionsSlide = new SimpleSlide.Builder()
                .title(R.string.permissions_title)
                .description(R.string.permissions_desc)
                .background(R.color.white)
                .permissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.GET_ACCOUNTS})
                .build();
                
        addSlide(permissionsSlide);
        
        addOnNavigationBlockedListener(new OnNavigationBlockedListener() {
            @Override
            public void onNavigationBlocked(int position, int direction) {
                View contentView = findViewById(android.R.id.content);
                if (contentView != null) {
                    Slide slide = getSlide(position);

                    if (slide.equals(permissionsSlide)) {
                        Snackbar.make(contentView, R.string.permissions_grant_error_text, Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });

    }
}
