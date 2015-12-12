package fr.pierrecavalet.bestexcuseever;

/**
 * Created by Pierre on 27/10/2015.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;

import fr.pierrecavalet.models.Bee;


public class BeeView extends CardView {

    private Bee mBee = null;
    private RelativeLayout mLayout = null;
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

        setMinimumHeight(400);
        mLayout = new RelativeLayout(getContext());
        // initialisation des TextView et ImageButton
        mBeeHeader = new TextView(getContext());
        mBeeHeader.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        mBeeHeader.setId(R.id.bee_view_header);
        RelativeLayout.LayoutParams headerParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayout.addView(mBeeHeader, headerParams);


        mBeeContent = new TextView(getContext());
        mBeeContent.setId(R.id.bee_view_content);
        RelativeLayout.LayoutParams contentParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        contentParams.addRule(mLayout.BELOW, R.id.bee_view_header);
        mLayout.addView(mBeeContent, contentParams);

        /*if(!hideButtons) {
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
        }*/

        // gestion du contenu de la vue en fonction de la bee
        if(mBee != null) {
            this.mBeeHeader.setText(mBee.getUser());
            this.mBeeContent.setText(mBee.getContent());

            // evenement pour demander les commentaires
            /*if(mSocket != null && !hideButtons) {
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
            }*/
        }

        addView(mLayout);
    }


    public void setmBeeContent(String s) {
        this.mBeeContent.setText(s);
    }

    public void setmBeeHeader(String s) {
        this.mBeeHeader.setText(s);
    }
}

