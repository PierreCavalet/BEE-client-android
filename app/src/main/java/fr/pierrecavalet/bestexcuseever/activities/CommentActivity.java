package fr.pierrecavalet.bestexcuseever.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.pierrecavalet.bestexcuseever.R;
import fr.pierrecavalet.bestexcuseever.models.Bee;
import fr.pierrecavalet.bestexcuseever.models.Comment;
import fr.pierrecavalet.bestexcuseever.sync.SocketHandler;
import fr.pierrecavalet.bestexcuseever.views.BeeView;
import fr.pierrecavalet.bestexcuseever.views.CommentsView;

public class CommentActivity extends AppCompatActivity {

    private Socket mSocket;
    private BeeView mBeeView;

    private Emitter.Listener onBeeCommentsList = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            CommentActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONArray commentsListJSON = (JSONArray) args[0];
                    LinearLayout layout = (LinearLayout) findViewById(R.id.commentLayout);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    try {
                        ArrayList<Comment> listComment = new ArrayList<Comment>();
                        for (int i = 0; i < commentsListJSON.length(); i++) {
                            JSONObject commentJSONObject = (JSONObject) commentsListJSON.get(i);
                            System.out.println("comment : " + commentJSONObject);
                            Comment comment = new Comment(commentJSONObject);
                            listComment.add(comment);
                        }
                        CommentsView commentsView = new CommentsView(CommentActivity.this, listComment);
                        layout.addView(commentsView);
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
                Bee bee = new Bee(beeJSONObject);
                mBeeView = new BeeView(CommentActivity.this, bee, mSocket);
                LinearLayout layout = (LinearLayout) findViewById(R.id.commentLayout);
                layout.addView(mBeeView);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
