package fr.pierrecavalet.bestexcuseever;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.nkzawa.socketio.client.Socket;

public class AddBeeActivity extends AppCompatActivity {

    private EditText mInputMessageView;
    private Socket mSocket;

    private void attemptSend() {
        String message = mInputMessageView.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            return;
        }
        mInputMessageView.setText("");
        mSocket.emit("test", message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bee);
        mInputMessageView = (EditText) findViewById(R.id.input);
        Button bouton = (Button) findViewById(R.id.button);
        bouton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        attemptSend();
                    }
                }
        );
    }
}
