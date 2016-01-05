package fr.pierrecavalet.bestexcuseever.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import fr.pierrecavalet.bestexcuseever.R;
import fr.pierrecavalet.bestexcuseever.activities.CommentActivity;
import fr.pierrecavalet.bestexcuseever.models.Bee;
import fr.pierrecavalet.bestexcuseever.sync.SocketHandler;
import fr.pierrecavalet.bestexcuseever.sync.UserHandler;

/**
 * Created by pierre on 16/12/15.
 */
public class BeeAdapter extends RecyclerView.Adapter<BeeAdapter.BeeViewHolder>{
    private List<Bee> mBees;
    private Activity mContext;
    private Socket mSocket;

    public BeeAdapter(List<Bee> bees, Activity context){
        this.mBees = bees;
        this.mContext = context;
        this.mSocket = SocketHandler.getSocket();
    }

    public void setBees(List<Bee> bees) {
        mBees = bees;
    }

    @Override
    public BeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_bee, parent, false);
        BeeViewHolder bvh = new BeeViewHolder(v);
        return bvh;
    }

    @Override
    public void onBindViewHolder(final BeeViewHolder holder, int position) {
        final Bee bee = mBees.get(position);
        holder.author.setText(bee.getUser());
        holder.content.setText(bee.getContent());
        holder.score.setText(String.valueOf(bee.getScore()));

        // cardview onClickListener
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, CommentActivity.class);
                // card transition
                String cardTransitionName = mContext.getString(R.string.card_transition);
                Pair<View, String> p1 = Pair.create((View) holder.cardView, cardTransitionName);

                // set up the transitions
                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(mContext, p1);
                try {
                    intent.putExtra("bee", bee.toJSONObject().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ActivityCompat.startActivity(mContext, intent, options.toBundle());
            }
        });

        // like onClickListener
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValueAnimator colorAnimationForward = ValueAnimator.ofObject(new ArgbEvaluator(),
                        ContextCompat.getColor(mContext, R.color.colorCardBackground),
                        ContextCompat.getColor(mContext, R.color.colorPrimary));
                colorAnimationForward.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        holder.cardView.setBackgroundColor((int) animator.getAnimatedValue());
                    }

                });
                colorAnimationForward.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ValueAnimator colorAnimationReverse = ValueAnimator.ofObject(new ArgbEvaluator(),
                                ContextCompat.getColor(mContext, R.color.colorPrimary),
                                ContextCompat.getColor(mContext, R.color.colorCardBackground));
                        colorAnimationReverse.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                            @Override
                            public void onAnimationUpdate(ValueAnimator animator) {
                                holder.cardView.setBackgroundColor((int) animator.getAnimatedValue());
                            }

                        });
                        colorAnimationReverse.start();
                    }
                });
                colorAnimationForward.start();
                holder.like.setEnabled(false);
                if(bee.getMyScore() == -1) {
                    bee.setScore(bee.getScore() + 2);
                } else {
                    bee.setScore(bee.getScore() + 1);
                }
                bee.setMyScore(1);
                holder.score.setText(String.valueOf(bee.getScore()));
                holder.dislike.setEnabled(true);

                JSONObject rateBee = new JSONObject();
                try {
                    rateBee.put("bee_id", bee.getId()).put("rate", 1);
                    mSocket.emit("rateBee", rateBee);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // dislike onClickListener
        holder.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValueAnimator colorAnimationForward = ValueAnimator.ofObject(new ArgbEvaluator(),
                        ContextCompat.getColor(mContext, R.color.colorCardBackground),
                        ContextCompat.getColor(mContext, R.color.colorAccent));
                colorAnimationForward.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        holder.cardView.setBackgroundColor((int) animator.getAnimatedValue());
                    }

                });
                colorAnimationForward.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ValueAnimator colorAnimationReverse = ValueAnimator.ofObject(new ArgbEvaluator(),
                                ContextCompat.getColor(mContext, R.color.colorAccent),
                                ContextCompat.getColor(mContext, R.color.colorCardBackground));
                        colorAnimationReverse.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                            @Override
                            public void onAnimationUpdate(ValueAnimator animator) {
                                holder.cardView.setBackgroundColor((int) animator.getAnimatedValue());
                            }

                        });
                        colorAnimationReverse.start();
                    }
                });
                colorAnimationForward.start();
                holder.dislike.setEnabled(false);
                if(bee.getMyScore() == 1) {
                    bee.setScore(bee.getScore() - 2);
                } else {
                    bee.setScore(bee.getScore() - 1);
                }
                bee.setMyScore(-1);
                holder.score.setText(String.valueOf(bee.getScore()));
                holder.like.setEnabled(true);

                JSONObject rateBee = new JSONObject();
                try {
                    rateBee.put("bee_id", bee.getId()).put("rate", -1);
                    mSocket.emit("rateBee", rateBee);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        if(bee.getMyScore() == -1) {
            holder.like.setEnabled(true);
        } else if (bee.getMyScore() == 1) {
            holder.dislike.setEnabled(true);
        } else {
            if(UserHandler.getUsername() != null) {
                holder.like.setEnabled(true);
                holder.dislike.setEnabled(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mBees.size();
    }

    public static class BeeViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView author;
        public TextView content;
        public TextView score;
        public Button like;
        public Button dislike;

        BeeViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            author = (TextView) itemView.findViewById(R.id.author);
            content = (TextView) itemView.findViewById(R.id.content);
            score = (TextView) itemView.findViewById(R.id.score);
            like = (Button) itemView.findViewById(R.id.like);
            dislike = (Button) itemView.findViewById(R.id.dislike);
        }
    }
}