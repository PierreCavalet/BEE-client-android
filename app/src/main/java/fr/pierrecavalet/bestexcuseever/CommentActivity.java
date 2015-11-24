package fr.pierrecavalet.bestexcuseever;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CommentActivity extends AppCompatActivity {

    private Socket mSocket;
    private BeeView mBeeView;

    private Emitter.Listener onBeeCommentsList = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            CommentActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("coms recus");
                    JSONArray commentsListJSON = (JSONArray) args[0];
                    try {
                        for (int i = 0; i < commentsListJSON.length(); i++) {
                            JSONObject commentJSONObject = (JSONObject) commentsListJSON.get(i);
                            System.out.println("content : " + commentJSONObject);
                        }
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
            System.out.println("if");
            String beeString = extras.getString("bee");
            try {
                JSONObject beeJSONObject = new JSONObject(beeString);
                Bee bee = new Bee(beeJSONObject);
                mBeeView = new BeeView(CommentActivity.this, bee, mSocket, true);
                LinearLayout layout = (LinearLayout) findViewById(R.id.commentLayout);
                layout.addView(mBeeView);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("ifnot");
        }
    }
}
