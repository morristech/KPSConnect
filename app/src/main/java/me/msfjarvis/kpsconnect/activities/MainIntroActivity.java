package me.msfjarvis.kpsconnect.activities;

import android.Manifest;
import android.os.Build;
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
                .image(R.drawable.ic_hint)
                .background(R.color.white)
                .build());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            final SimpleSlide storagePermissionsSlide = new SimpleSlide.Builder()
                    .title(R.string.permissions_storage_title)
                    .description(R.string.permissions_storage_desc)
                    .background(R.color.white)
                    .permissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE})
                    .build();

            final SimpleSlide contactPermissionsSlide = new SimpleSlide.Builder()
                    .title(R.string.permissions_contact_title)
                    .description(R.string.permissions_contact_desc)
                    .background(R.color.white)
                    .permissions(new String[] {Manifest.permission.GET_ACCOUNTS})
                    .build();

            addSlide(storagePermissionsSlide);
            addSlide(contactPermissionsSlide);

            addOnNavigationBlockedListener(new OnNavigationBlockedListener() {
                @Override
                public void onNavigationBlocked(int position, int direction) {
                    View contentView = findViewById(android.R.id.content);
                    if (contentView != null) {
                        Slide slide = getSlide(position);

                        if (slide.equals(contactPermissionsSlide) || slide.equals(storagePermissionsSlide)) {
                            Snackbar.make(contentView, R.string.permissions_grant_error_text, Snackbar.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }

    }
}
