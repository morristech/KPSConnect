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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CreateCard extends Activity{
    public CardView newCard(Context mContext, int mRadius, int mPadding, int mMaxElevation, int mElevation, String mBackgroundColor, String mTitle, int mTitleSize, String mTitleColor, final String onClickURL){
        CardView card = new CardView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        card.setLayoutParams(params);
        card.setRadius(mRadius);
        card.setContentPadding(mPadding, mPadding, mPadding, mPadding);
        card.setCardBackgroundColor(Color.parseColor(mBackgroundColor));
        card.setMaxCardElevation(mMaxElevation);
        card.setCardElevation(mElevation);
        TextView tv = new TextView(mContext);
        tv.setLayoutParams(params);
        tv.setText(mTitle);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mTitleSize);
        tv.setTextColor(Color.parseColor(mTitleColor));
        card.addView(tv);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(onClickURL));
                try{
                    startActivity(intent);
                }catch(ActivityNotFoundException exc){
                    Toast.makeText(view.getContext(),exc.toString(),Toast.LENGTH_LONG).show();
                }
            }
        });
        return card;
    }
}
