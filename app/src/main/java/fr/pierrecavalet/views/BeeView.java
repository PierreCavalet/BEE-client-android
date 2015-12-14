package fr.pierrecavalet.views;

/**
 * Created by Pierre on 27/10/2015.
 */

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.nkzawa.socketio.client.Socket;

import fr.pierrecavalet.bestexcuseever.R;
import fr.pierrecavalet.models.Bee;


public class BeeView extends CardView {

    private Bee mBee = null;
    private RelativeLayout mLayout = null;

    private TextView mContent = null;
    private TextView mAuthor = null;
    private TextView mDate = null;
    private TextView mLocation = null;

    private ImageView mDateImage = null;
    private ImageView mLocationImage = null;
    private ImageView mAuthorImage = null;

    private CardView mLike = null;
    private CardView mDislike = null;
    private CardView mComments = null;
    private Socket mSocket = null;


    public BeeView(Context context) {
        super(context);
        init();
    }

    public BeeView(Context context, Bee bee, Socket socket) {
        super(context);
        this.mBee = bee;
        this.mSocket = socket;
        init();
    }


    public BeeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        // layout
        mLayout = new RelativeLayout(getContext());
        mLayout.setPadding(30, 10, 30, 10);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mLayout, layoutParams);

        // Author img
        mAuthorImage =  new ImageView(getContext());
        mAuthorImage.setPadding(0, 10, 0, 20);
        mAuthorImage.setId(R.id.bee_view_image_author);
        mAuthorImage.setImageResource(R.drawable.ic_create_black_36dp);
        RelativeLayout.LayoutParams imageAuthorParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        imageAuthorParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        mLayout.addView(mAuthorImage, imageAuthorParams);

        // Author
        mAuthor = new TextView(getContext());
        mAuthor.setId(R.id.bee_view_author);
        mAuthor.setTextSize(36);
        mAuthor.setTextColor(Color.BLACK);
        RelativeLayout.LayoutParams authorParams = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        authorParams.addRule(mLayout.RIGHT_OF, R.id.bee_view_image_author);
        mLayout.addView(mAuthor, authorParams);

        // content
        mContent = new TextView(getContext());
        mContent.setId(R.id.bee_view_content);
        mContent.setPadding(0, 0, 0, 30);
        RelativeLayout.LayoutParams contentParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        contentParams.addRule(mLayout.BELOW, R.id.bee_view_image_author);
        mLayout.addView(mContent, contentParams);

        // date img
        mDateImage = new ImageView(getContext());
        mDateImage.setId(R.id.bee_view_image_date);
        mDateImage.setImageResource(R.drawable.ic_today_black_24dp);
        RelativeLayout.LayoutParams imageDateParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageDateParams.addRule(mLayout.BELOW, R.id.bee_view_content);
        mLayout.addView(mDateImage, imageDateParams);

        // date text
        mDate = new TextView(getContext());
        mDate.setId(R.id.bee_view_date);
        mDate.setTextSize(16);
        RelativeLayout.LayoutParams dateParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dateParams.addRule(mLayout.BELOW, R.id.bee_view_content);
        dateParams.addRule(mLayout.RIGHT_OF, R.id.bee_view_image_date);
        mLayout.addView(mDate, dateParams);

        // location img
        mLocationImage = new ImageView(getContext());
        mLocationImage.setId(R.id.bee_view_image_location);
        mLocationImage.setImageResource(R.drawable.ic_place_black_24dp);
        mLocationImage.setPadding(0, 0, 0, 30);
        RelativeLayout.LayoutParams imageLocationParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageLocationParams.addRule(mLayout.BELOW, R.id.bee_view_image_date);
        mLayout.addView(mLocationImage, imageLocationParams);

        // location text
        mLocation = new TextView(getContext());
        mLocation.setTextSize(16);
        mLocation.setId(R.id.bee_view_location);
        RelativeLayout.LayoutParams locationParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        locationParams.addRule(mLayout.BELOW, R.id.bee_view_image_date);
        locationParams.addRule(mLayout.RIGHT_OF, R.id.bee_view_image_location);
        mLayout.addView(mLocation, locationParams);


        // Dislike button
        TextView tvDislike = new TextView(getContext());
        tvDislike.setText("DISLIKE");
        tvDislike.setPadding(12, 8, 12, 8);
        tvDislike.setTextSize(18);
        tvDislike.setTextColor(Color.WHITE);
        mDislike = new CardView(getContext());
        mDislike.addView(tvDislike);
        mDislike.setCardBackgroundColor(Color.RED);
        mDislike.setId(R.id.bee_view_dislike);
        RelativeLayout.LayoutParams dislikeParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dislikeParams.addRule(mLayout.BELOW, R.id.bee_view_image_location);
        dislikeParams.addRule(mLayout.LEFT_OF, R.id.bee_view_like);
        mLayout.addView(mDislike, dislikeParams);

        // Like button
        TextView tvLike = new TextView(getContext());
        tvLike.setText("LIKE");
        tvLike.setPadding(12, 8, 12, 8);
        tvLike.setTextSize(18);
        tvLike.setTextColor(Color.WHITE);
        mLike = new CardView(getContext());
        mLike.addView(tvLike);
        mLike.setCardBackgroundColor(Color.rgb(0, 168, 34));
        mLike.setId(R.id.bee_view_like);
        RelativeLayout.LayoutParams likeParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        likeParams.addRule(mLayout.BELOW, R.id.bee_view_image_location);
        likeParams.addRule(mLayout.LEFT_OF, R.id.bee_view_comment);
        mLayout.addView(mLike, likeParams);

        // Comments button
        TextView tvComment = new TextView(getContext());
        tvComment.setText("COMS");
        tvComment.setPadding(12, 8, 12, 8);
        tvComment.setTextSize(18);
        tvComment.setTextColor(Color.WHITE);
        mComments = new CardView(getContext());
        mComments.addView(tvComment);
        mComments.setId(R.id.bee_view_comment);
        mComments.setCardBackgroundColor(Color.rgb(0, 127, 254));
        RelativeLayout.LayoutParams commentsParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        commentsParams.addRule(mLayout.BELOW, R.id.bee_view_image_location);
        commentsParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mLayout.addView(mComments, commentsParams);





        /*mLike = new ImageButton(getContext());
        mLike.setImageResource(R.drawable.ic_exposure_plus_1_black_24dp);
        mLike.setId(R.id.bee_view_like);

        mHate = new ImageButton(getContext());
        mHate.setImageResource(R.drawable.ic_exposure_neg_1_black_24dp);
        mHate.setId(R.id.bee_view_hate);*/

        /*mComments = new ImageButton(getContext());
        mComments.setImageResource(R.drawable.ic_comment_black_24dp);
        mComments.setId(R.id.bee_view_comment);



        RelativeLayout.LayoutParams commentsParam = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        commentsParam.addRule(mLayout.BELOW, R.id.bee_view_content);
        commentsParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        addView(mComments, commentsParam);*/

        /*RelativeLayout.LayoutParams hateParams = new RelativeLayout.LayoutParams(wrap, wrap);
        hateParams.addRule(LEFT_OF, R.id.bee_view_comment);
        hateParams.addRule(BELOW, R.id.bee_view_content);
        addView(mHate, hateParams);

        RelativeLayout.LayoutParams likeParams = new RelativeLayout.LayoutParams(wrap, wrap);
        likeParams.addRule(BELOW, R.id.bee_view_content);
        likeParams.addRule(LEFT_OF, R.id.bee_view_hate);
        addView(mLike, likeParams);*/


        // gestion du contenu de la vue en fonction de la bee
        if(mBee != null) {
            this.mAuthor.setText(mBee.getUser());
            this.mContent.setText(mBee.getContent());
            this.mDate.setText("15/05/2015");
            this.mLocation.setText("Latour-Bas-Elne");
            // evenement pour demander les commentaires
            if(mSocket != null) {
                /*mComments.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        mSocket.emit("askBeeComments", mBee.getId());
                        Intent beeActivity = new Intent(getContext(), BeeActivity.class);
                        try {
                            beeActivity.putExtra("bee", mBee.toJSONObject().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        getContext().startActivity(beeActivity);
                    }
                });*/
            }
        }

    }


    public void setmContent(String s) {
        this.mContent.setText(s);
    }

    public void setmAuthor(String s) {
        this.mAuthor.setText(s);
    }
}

