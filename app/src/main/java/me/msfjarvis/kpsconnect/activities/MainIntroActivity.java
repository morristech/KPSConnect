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
                .background(R.color.colorPrimary)
                .build());
                
        addSlide(new SimpleSlide.Builder()
                .title("Usage")
                .description("Tap on card to read the post, long press to share")
                // TODO Add in necessary image
                .image(R.drawable.ic_launcher)
                .background(R.color.colorPrimary)
                .build());
                
        final SimpleSlide permissionsSlide = new SimpleSlide.Builder()
                .title("Storage Permissions")
                .description("The identifier token is stored on the device itself, hence we need storage permissions")
                .background(R.color.colorPrimary)
                .permissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE})
                .build();
                
        addSlide(permissionsSlide);
        
        addOnNavigationBlockedListener(new OnNavigationBlockedListener() {
            @Override
            public void onNavigationBlocked(int position, int direction) {
                View contentView = findViewById(android.R.id.content);
                if (contentView != null) {
                    Slide slide = getSlide(position);

                    if (slide.equals(permissionsSlide)) {
                        Snackbar.make(contentView, "You need to grant the permission first!", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });

    }
}
