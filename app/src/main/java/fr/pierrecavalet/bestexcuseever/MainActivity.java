package fr.pierrecavalet.bestexcuseever;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    private Socket mSocket;
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
        try {
            SocketHandler.setSocket(IO.socket("http://149.202.49.136:1337"));
        } catch (URISyntaxException e) {}
        mSocket = SocketHandler.getSocket();
        mSocket.on("beesList", onBeesList);
        mSocket.connect();
        setContentView(R.layout.activity_main);
        Button ask = (Button) findViewById(R.id.ask_button);
        ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSocket.emit("askBeesList");
            }
        });
        Button switch_to_send = (Button) findViewById(R.id.switch_to_send_button);
        switch_to_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addBeeActivity = new Intent(MainActivity.this, AddBeeActivity.class);
                startActivity(addBeeActivity);
            }
        });
        Button switch_to_sign_in = (Button) findViewById(R.id.switch_to_sign_in_button);
        switch_to_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInActivity = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(signInActivity);
            }
        });
        Button switch_to_sign_up = (Button) findViewById(R.id.switch_to_sign_up_button);
        switch_to_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpActivity = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(signUpActivity);
            }
        });
        mOutputMessageView = (TextView) findViewById(R.id.output);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
        mSocket.off("beesList", onBeesList);
    }

}
