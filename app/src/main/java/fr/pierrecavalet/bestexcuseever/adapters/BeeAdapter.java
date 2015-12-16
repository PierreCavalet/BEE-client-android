package fr.pierrecavalet.bestexcuseever.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

import java.util.List;

import fr.pierrecavalet.bestexcuseever.R;
import fr.pierrecavalet.bestexcuseever.activities.CommentActivity;
import fr.pierrecavalet.bestexcuseever.models.Bee;

/**
 * Created by pierre on 16/12/15.
 */
public class BeeAdapter extends RecyclerView.Adapter<BeeAdapter.BeeViewHolder>{
    private List<Bee> mBees;
    private Activity mContext;

    public BeeAdapter(List<Bee> bees, Activity context){
        this.mBees = bees;
        this.mContext = context;
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
        View v = holder.cardView;
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
    }

    @Override
    public int getItemCount() {
        return mBees.size();
    }

    public static class BeeViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView author;
        public TextView content;
        public Button like;
        public Button dislike;

        BeeViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            author = (TextView) itemView.findViewById(R.id.author);
            content = (TextView) itemView.findViewById(R.id.content);
            like = (Button) itemView.findViewById(R.id.like);
            dislike = (Button) itemView.findViewById(R.id.dislike);
        }
    }

    public static void setButtonTint(Button button, ColorStateList tint) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP && button instanceof AppCompatButton) {
            ((AppCompatButton) button).setSupportBackgroundTintList(tint);
        } else {
            ViewCompat.setBackgroundTintList(button, tint);
        }
    }


}