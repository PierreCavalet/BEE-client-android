package fr.pierrecavalet.bestexcuseever;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {

    private EditText mInputAccount;
    private EditText mInputPassword;
    private Socket mSocket;

    private void attemptSend() throws JSONException {
        String account = mInputAccount.getText().toString().trim();
        String password = mInputPassword.getText().toString().trim();
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
            return;
        }

        JSONObject jo = new JSONObject();
        jo.put("account", account);
        jo.put("password", password);
        mSocket.emit("signUp", jo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mSocket = SocketHandler.getSocket();

        mInputAccount = (EditText) findViewById(R.id.account_input);
        mInputPassword = (EditText) findViewById(R.id.password_input);
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
