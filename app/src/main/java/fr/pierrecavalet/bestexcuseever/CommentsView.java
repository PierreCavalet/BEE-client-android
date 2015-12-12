package fr.pierrecavalet.bestexcuseever;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import fr.pierrecavalet.models.Comment;

/**
 * Created by pierre on 24/11/15.
 */
public class CommentsView extends LinearLayout {

    ArrayList<Comment> mComments = null;

    public CommentsView(Context context) {
        super(context);
    }

    public CommentsView(Context context, ArrayList<Comment> comments) {
        super(context);
        mComments = comments;
        init();
    }

    public void init() {
        for(Comment comment : mComments) {
            int wrap = LinearLayout.LayoutParams.WRAP_CONTENT;
            int match = LinearLayout.LayoutParams.MATCH_PARENT;
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(match, wrap);
            param.setMargins(0, 30, 0, 10);
            setLayoutParams(param);
            setOrientation(LinearLayout.VERTICAL);

            // border + fond blanc
            setBackgroundResource(R.drawable.custom_background);

            TextView name = new TextView(getContext());
            name.setTextColor(Color.BLACK);
            name.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            name.setText(comment.getUsername());
            name.setBackgroundColor(100);

            TextView content = new TextView(getContext());
            content.setTextColor(Color.BLACK);
            content.setBackgroundColor(3);
            content.setText(comment.getContent());

            LinearLayout.LayoutParams paramName = new LinearLayout.LayoutParams(wrap, wrap);
            addView(name, paramName);
            addView(content, paramName);
        }
    }
}
