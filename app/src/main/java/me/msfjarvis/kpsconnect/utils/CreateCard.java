package me.msfjarvis.kpsconnect.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import com.google.android.material.snackbar.Snackbar;
import androidx.cardview.widget.CardView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import me.msfjarvis.kpsconnect.R;
import me.msfjarvis.kpsconnect.activities.FeedActivity;

public class CreateCard extends Activity {

    public static int dpToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static CardView newCard(final Context mContext, final Activity activity,
                                   int mRadius, int mPadding,
                                   int mMaxElevation, int mElevation, String mBackgroundColor,
                                   final String mTitle, int mTitleSize, String mTitleColor,
                                   final String mCategory, int mCatSize, String mCatColor,
                                   final String onClickURL, ImageView imageView,
                                   final String mContent, final String mImageURL) {
        CardView card = new CardView(mContext);
        RelativeLayout inner = new RelativeLayout(mContext);
        LinearLayout.LayoutParams innerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        RelativeLayout.LayoutParams tvParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        RelativeLayout.LayoutParams tvcParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        RelativeLayout.LayoutParams imgParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        int cm = dpToPx(mContext, 16);
        cardParams.setMargins(cm, cm, cm, cm);
        inner.setLayoutParams(innerParams);
        card.setLayoutParams(cardParams);
        card.setRadius(mRadius);
        card.setContentPadding(mPadding, mPadding, mPadding, mPadding);
        card.setCardBackgroundColor(Color.parseColor(mBackgroundColor));
        card.setMaxCardElevation(mMaxElevation);
        card.setCardElevation(mElevation);
        int tvMargins = dpToPx(mContext, 2);
        TextView tv = new TextView(mContext);
        tvParams.setMargins(tvMargins, tvMargins, tvMargins, tvMargins);
        tv.setLayoutParams(tvParams);
        tv.setText(mTitle);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mTitleSize);
        tv.setTextColor(Color.parseColor(mTitleColor));
        inner.addView(tv);
        tv.invalidate();
        //noinspection ResourceType
        tv.setId(1092814);
        //noinspection ResourceType
        imageView.setId(1092816);
        imgParams.addRule(RelativeLayout.BELOW, tv.getId());
        imgParams.setMargins(0, tvMargins, 0, tvMargins);
        imageView.setLayoutParams(imgParams);
        inner.addView(imageView);
        TextView tvc = new TextView(mContext);
        tvcParams.setMargins(tvMargins, tvMargins * 2, tvMargins, tvMargins);
        tvcParams.addRule(RelativeLayout.BELOW, imageView.getId());
        tvc.setLayoutParams(tvcParams);
        tvc.setText(mCategory);
        tvc.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mCatSize);
        tvc.setTextColor(Color.parseColor(mCatColor));
        inner.addView(tvc);
        card.addView(inner);
        card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, onClickURL);
                try {
                    activity.startActivity(Intent.createChooser(shareIntent, "Share link using"));
                }catch(Exception exc){
                    Toast.makeText(mContext,exc.toString(),Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
        //category specific hack
        //I-Lead newsletter posts have no content
        if (mCategory.equals("I-Lead")) {
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view,R.string.i_lead_category_hack_error_text,Snackbar.LENGTH_LONG).show();
                }
            });
        }else{
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, FeedActivity.class);
                    intent.putExtra("title",mTitle);
                    intent.putExtra("category",mCategory);
                    intent.putExtra("content",mContent);
                    intent.putExtra("featuredImage",mImageURL);
                    try{
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.slide_up_info,R.anim.no_change);
                    }catch(Exception exc){
                        Toast.makeText(mContext,exc.toString(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        return card;
    }
}
