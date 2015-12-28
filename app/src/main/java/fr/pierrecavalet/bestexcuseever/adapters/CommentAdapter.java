package fr.pierrecavalet.bestexcuseever.adapters;

import android.content.res.ColorStateList;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import fr.pierrecavalet.bestexcuseever.R;
import fr.pierrecavalet.bestexcuseever.models.Comment;

/**
 * Created by pierre on 16/12/15.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{
    private List<Comment> mComments;
    private static int selectedItem = -1;

    public CommentAdapter(List<Comment> comments){
        this.mComments = comments;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_comment, parent, false);
        CommentViewHolder cvh = new CommentViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        final Comment comment = mComments.get(position);
        holder.author.setText(comment.getUsername());
        holder.content.setText(comment.getContent());
        if(position != 0) {
            holder.lineTop.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        public TextView author;
        public TextView content;
        public View lineTop;
        public View lineBottom;

        CommentViewHolder(View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.comment_author);
            content = (TextView) itemView.findViewById(R.id.comment_content);
            lineTop = itemView.findViewById(R.id.line_top);
            lineBottom = itemView.findViewById(R.id.line_bottom);
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