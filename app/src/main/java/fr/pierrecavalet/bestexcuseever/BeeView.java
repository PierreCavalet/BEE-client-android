package fr.pierrecavalet.bestexcuseever;

/**
 * Created by Pierre on 27/10/2015.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;


public class BeeView extends RelativeLayout {

    private Bee mBee = null;
    private TextView mBeeContent = null;
    private TextView mBeeHeader = null;
    private ImageButton mLike = null;
    private ImageButton mHate = null;
    private ImageButton mComments = null;
    private Socket mSocket = null;
    private boolean hideButtons = false;


    public BeeView(Context context) {
        super(context);
        init();
    }

    public BeeView(Context context, Bee bee, Socket socket, boolean hide) {
        super(context);
        this.mBee = bee;
        this.mSocket = socket;
        this.hideButtons = hide;
        init();
    }


    public BeeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        int wrap = LayoutParams.WRAP_CONTENT;
        int fill = LayoutParams.FILL_PARENT;
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(fill, wrap);
        param.setMargins(0, 30, 0, 10);
        setLayoutParams(param);

        // border + fond blanc
        setBackgroundResource(R.drawable.custom_background);

        // initialisation des TextView et ImageButton
        mBeeHeader = new TextView(getContext());
        mBeeHeader.setTextColor(Color.BLACK);
        mBeeHeader.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        mBeeHeader.setBackgroundResource(R.drawable.custom_background_header);
        mBeeHeader.setId(R.id.bee_view_header);

        mBeeContent = new TextView(getContext());
        mBeeContent.setTextColor(Color.BLACK);
        mBeeContent.setBackgroundResource(R.drawable.custom_background_content);
        mBeeContent.setId(R.id.bee_view_content);

        if(!hideButtons) {
            mLike = new ImageButton(getContext());
            mLike.setImageResource(R.drawable.ic_exposure_plus_1_black_24dp);
            mLike.setId(R.id.bee_view_like);

            mHate = new ImageButton(getContext());
            mHate.setImageResource(R.drawable.ic_exposure_neg_1_black_24dp);
            mHate.setId(R.id.bee_view_hate);

            mComments = new ImageButton(getContext());
            mComments.setImageResource(R.drawable.ic_comment_black_24dp);
            mComments.setId(R.id.bee_view_comment);
        }



        // insertion et placement des TextView et ImageButton sur le layout
        RelativeLayout.LayoutParams headerParams = new RelativeLayout.LayoutParams(fill, wrap);
        addView(mBeeHeader, headerParams);

        RelativeLayout.LayoutParams contentParams = new RelativeLayout.LayoutParams(fill, wrap);
        contentParams.addRule(BELOW, R.id.bee_view_header);
        addView(mBeeContent, contentParams);

        if(!hideButtons) {
            RelativeLayout.LayoutParams commentsParam = new RelativeLayout.LayoutParams(wrap, wrap);
            commentsParam.addRule(BELOW, R.id.bee_view_content);
            commentsParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            addView(mComments, commentsParam);

            RelativeLayout.LayoutParams hateParams = new RelativeLayout.LayoutParams(wrap, wrap);
            hateParams.addRule(LEFT_OF, R.id.bee_view_comment);
            hateParams.addRule(BELOW, R.id.bee_view_content);
            addView(mHate, hateParams);

            RelativeLayout.LayoutParams likeParams = new RelativeLayout.LayoutParams(wrap, wrap);
            likeParams.addRule(BELOW, R.id.bee_view_content);
            likeParams.addRule(LEFT_OF, R.id.bee_view_hate);
            addView(mLike, likeParams);
        }

        // gestion du contenu de la vue en fonction de la bee
        if(mBee != null) {
            this.mBeeHeader.setText(mBee.getUser());
            this.mBeeContent.setText(mBee.getContent());

            // evenement pour demander les commentaires
            if(mSocket != null && !hideButtons) {
                mComments.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        mSocket.emit("askBeeComments", mBee.getId());
                        Intent commentActivity = new Intent(getContext(), CommentActivity.class);
                        try {
                            commentActivity.putExtra("bee", mBee.toJSONObject().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        getContext().startActivity(commentActivity);
                    }
                });
            }
        }
    }


    public void setmBeeContent(String s) {
        this.mBeeContent.setText(s);
    }

    public void setmBeeHeader(String s) {
        this.mBeeHeader.setText(s);
    }
}

