package fr.pierrecavalet.bestexcuseever;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://149.202.49.136:1337");
        } catch (URISyntaxException e) {}
    }

    private TextView mOutputMessageView;



    private Emitter.Listener onBeesList = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONArray beesListJSON = (JSONArray) args[0];
                    try {
                        for (int i = 0; i < beesListJSON.length(); i++) {
                            JSONObject beeJSONObject = (JSONObject) beesListJSON.get(i);
                            Bee bee = new Bee(beeJSONObject);
                            mOutputMessageView.setText(mOutputMessageView.getText() + " " + bee.getContent());
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
        mSocket.on("beesList", onBeesList);
        mSocket.connect();
        setContentView(R.layout.activity_main);
        Button bouton = (Button) findViewById(R.id.button);
        bouton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSocket.emit("askBeesList");
                    }
                }
        );
        mOutputMessageView = (TextView) findViewById(R.id.output);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
        mSocket.off("beesList", onBeesList);
    }

}
