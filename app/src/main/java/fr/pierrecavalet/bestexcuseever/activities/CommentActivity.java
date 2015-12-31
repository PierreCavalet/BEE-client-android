package fr.pierrecavalet.bestexcuseever.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.pierrecavalet.bestexcuseever.R;
import fr.pierrecavalet.bestexcuseever.adapters.CommentAdapter;
import fr.pierrecavalet.bestexcuseever.models.Bee;
import fr.pierrecavalet.bestexcuseever.models.Comment;
import fr.pierrecavalet.bestexcuseever.sync.SocketHandler;

public class CommentActivity extends AppCompatActivity {

    private Socket mSocket;
    private Bee mBee;
    private ArrayList<Comment> mComments = new ArrayList<Comment>();
    private TextView mAuthor;
    private TextView mContent;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Emitter.Listener onBeeCommentsList = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            CommentActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONArray commentsListJSON = (JSONArray) args[0];
                    try {
                        for (int i = 0; i < commentsListJSON.length(); i++) {
                            JSONObject commentJSONObject = (JSONObject) commentsListJSON.get(i);
                            System.out.println("comment : " + commentJSONObject);
                            Comment comment = new Comment(commentJSONObject);
                            mComments.add(comment);
                        }
                        mAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        //récupération de la socket + bind event
        mSocket = SocketHandler.getSocket();
        mSocket.on("beeCommentsList", onBeeCommentsList);

        // récupération de la bee
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String beeString = extras.getString("bee");
            try {
                JSONObject beeJSONObject = new JSONObject(beeString);
                mBee = new Bee(beeJSONObject);
                mAuthor = (TextView) findViewById(R.id.author);
                mAuthor.setText(mBee.getUser());
                mContent = (TextView) findViewById(R.id.content);
                mContent.setText(mBee.getContent());
                mSocket.emit("askBeeComments", mBee.getId());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.comment_recycler_view);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new CommentAdapter(mComments);
        mRecyclerView.setAdapter(mAdapter);
    }


}
