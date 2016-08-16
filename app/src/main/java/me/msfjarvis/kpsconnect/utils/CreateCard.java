package me.msfjarvis.kpsconnect.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CreateCard extends Activity {
    public static int dpToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static CardView newCard(final Context mContext, final Activity activity,
                                   int mRadius, int mPadding,
                                   int mMaxElevation, int mElevation, String mBackgroundColor,
                                   String mTitle, int mTitleSize, String mTitleColor,
                                   String mCategory, int mCatSize, String mCatColor,
                                   final String onClickURL, ImageView imageView) {
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
        int cm = dpToPx(mContext, 4);
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
        tvcParams.setMargins(tvMargins, tvMargins*2, tvMargins, tvMargins);
        tvcParams.addRule(RelativeLayout.BELOW, imageView.getId());
        tvc.setLayoutParams(tvcParams);
        tvc.setText(mCategory);
        tvc.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mCatSize);
        tvc.setTextColor(Color.parseColor(mCatColor));
        inner.addView(tvc);
        card.addView(inner);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(onClickURL));
                try {
                    activity.startActivity(intent);
                } catch(ActivityNotFoundException exc) {
                    Toast.makeText(view.getContext(), exc.toString(),Toast.LENGTH_LONG).show();
                }
            }
        });
        return card;
    }
}
