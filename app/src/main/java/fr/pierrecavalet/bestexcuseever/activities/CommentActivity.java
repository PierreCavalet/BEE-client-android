package fr.pierrecavalet.bestexcuseever.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.UserManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import fr.pierrecavalet.bestexcuseever.R;
import fr.pierrecavalet.bestexcuseever.adapters.CommentAdapter;
import fr.pierrecavalet.bestexcuseever.models.Bee;
import fr.pierrecavalet.bestexcuseever.models.Comment;
import fr.pierrecavalet.bestexcuseever.sync.SocketHandler;
import fr.pierrecavalet.bestexcuseever.sync.UserHandler;

public class CommentActivity extends CustomActionBarActivity {

    private Socket mSocket;
    private Bee mBee;
    private ArrayList<Comment> mComments = new ArrayList<Comment>();
    private TextView mAuthor;
    private TextView mContent;
    private TextView mPhone;
    private TextView mMail;
    private TextView mScore;
    private Button mSend;
    private EditText mMessage;
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

        // email address and phone number
        mMail = (TextView) findViewById(R.id.mail);
        mPhone = (TextView) findViewById(R.id.phone);
        mMail.setText(Html.fromHtml("<a href=\"mailto:mail@gmail.com\">mail@gmail.com</a>"));
        mMail.setMovementMethod(LinkMovementMethod.getInstance());
        mPhone.setText(Html.fromHtml("<a href=\"tel:0123456789\">0123456789</a>"));
        mPhone.setMovementMethod(LinkMovementMethod.getInstance());


        // send comment
        mSend = (Button) findViewById(R.id.send_button);
        mMessage = (EditText) findViewById(R.id.comment_edit);
        String user = UserHandler.getUsername();
        if(user != null) {

            mSend.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                attemptSend();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            );
        } else {
            mSend.setVisibility(View.GONE);
            mMessage.setVisibility(View.GONE);
        }



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
                mScore = (TextView) findViewById(R.id.score);
                mScore.setText(String.valueOf(mBee.getScore()));
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

    private void attemptSend() throws JSONException {
        String message = mMessage.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            return;
        }
        mMessage.setText("");
        Comment comment = new Comment(mBee.getId(), UserHandler.getUsername(), message);
        mSocket.emit("sendComment", comment.toJSONObject());
    }


}
