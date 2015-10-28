package fr.pierrecavalet.bestexcuseever;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

public class SignInActivity extends AppCompatActivity {

    private EditText mInputAccount;
    private EditText mInputPassword;
    private Socket mSocket;
    private String account = null;

    private Emitter.Listener onSignUpResult = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            SignInActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if ((int) args[0] == 1) {
                        TextView logs = (TextView) findViewById(R.id.logs);
                        logs.setText("Votre compte a bien été créé. Vous pouvez maintenant vous connecter.");
                    }
                }
            });
        }
    };

    private Emitter.Listener onSignInResult = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            SignInActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if ((int) args[0] == 1) {
                        UserHandler.setUsername(account);
                        finish();
                    }
                }
            });
        }
    };

    private void attemptSend() throws JSONException {
        String account = mInputAccount.getText().toString().trim();
        String password = mInputPassword.getText().toString().trim();
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
            return;
        }
        this.account = account;
        JSONObject jo = new JSONObject();
        jo.put("account", account);
        jo.put("password", password);
        mSocket.emit("signIn", jo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mSocket = SocketHandler.getSocket();
        mSocket.on("signUpResult", onSignUpResult);
        mSocket.on("signInResult", onSignInResult);

        // ajoute le bouton de retour à l'activity parent
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mInputAccount = (EditText) findViewById(R.id.account_input);
        mInputPassword = (EditText) findViewById(R.id.password_input);

        // bouton de connexion
        Button send = (Button) findViewById(R.id.send_button);
        send.setOnClickListener(new View.OnClickListener() {
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

        // bouton d'inscription
        Button signup = (Button) findViewById(R.id.sign_up_button);
        signup.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent signInActivity = new Intent(SignInActivity.this, SignUpActivity.class);
                        startActivity(signInActivity);
                    }
                }
        );
    }
}
