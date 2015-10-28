package fr.pierrecavalet.bestexcuseever;

/**
 * Created by Pierre on 27/10/2015.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BeeView extends LinearLayout {

    private TextView mBeeContent = null;
    private TextView mBeeRest = null;


    public BeeView(Context context) {
        super(context);
        init();
    }


    public BeeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        int wrap = LayoutParams.WRAP_CONTENT;
        int fill = LayoutParams.FILL_PARENT;

        setOrientation(LinearLayout.VERTICAL);
        setLayoutParams(new LayoutParams(fill, wrap));
        setBackgroundResource(R.drawable.custom_background);

        mBeeContent = new TextView(getContext());
        mBeeContent.setTextColor(Color.BLACK);
        mBeeRest = new TextView(getContext());
        mBeeRest.setTextColor(Color.BLACK);
        mBeeRest.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));


        mBeeContent.setText("content");
        mBeeRest.setText("rest");

        addView(mBeeRest, new LinearLayout.LayoutParams(fill, wrap));
        addView(mBeeContent, new LinearLayout.LayoutParams(fill, wrap));
    }

    public void setmBeeContent(String s) {
        this.mBeeContent.setText(s);
    }

    public void setmBeeRest(String s) {
        this.mBeeRest.setText(s);
    }
}

