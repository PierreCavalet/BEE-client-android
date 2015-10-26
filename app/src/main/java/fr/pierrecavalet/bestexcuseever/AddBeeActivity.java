package fr.pierrecavalet.bestexcuseever;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

public class AddBeeActivity extends AppCompatActivity {

    private EditText mInputMessageView;
    private Socket mSocket;

    private void attemptSend() throws JSONException {
        String message = mInputMessageView.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            return;
        }
        Bee bee = new Bee("Odette", "Sauvetat", "10h" , message, 1000, 0);
        mInputMessageView.setText("");
        mSocket.emit("sendBee", bee.toJSONObject());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bee);
        mSocket = SocketHandler.getSocket();

        mInputMessageView = (EditText) findViewById(R.id.input);
        Button bouton = (Button) findViewById(R.id.send_button);
        bouton.setOnClickListener(
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
    }
}
