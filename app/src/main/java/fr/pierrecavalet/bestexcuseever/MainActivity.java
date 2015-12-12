package fr.pierrecavalet.bestexcuseever;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import fr.pierrecavalet.models.Bee;
import fr.pierrecavalet.sync.SocketHandler;
import fr.pierrecavalet.sync.UserHandler;

public class MainActivity extends AppCompatActivity {

    private Socket mSocket;
    private ArrayList<BeeView> mBeeViewList = new ArrayList<BeeView>();
    private Menu mMenu;


    private Emitter.Listener onBeesList = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONArray beesListJSON = (JSONArray) args[0];
                    LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    try {
                        for (int i = 0; i < beesListJSON.length(); i++) {
                            JSONObject beeJSONObject = (JSONObject) beesListJSON.get(i);
                            Bee bee = new Bee(beeJSONObject);
                            BeeView beeView = new BeeView(MainActivity.this, bee, mSocket, false);
                            mBeeViewList.add(beeView);
                            layout.addView(beeView);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    private Emitter.Listener onSignInResult = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if ((int) args[0] == 1) {
                        MenuItem writeBee = mMenu.findItem(R.id.action_write_bee);
                        writeBee.setVisible(true);
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
        mSocket.on("signInResult", onSignInResult);
        mSocket.connect();
        setContentView(R.layout.activity_main);
        mSocket.emit("askBeesList");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (UserHandler.getUsername() == null) {
            MenuItem writeBee = menu.findItem(R.id.action_write_bee);
            writeBee.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_write_bee:
                writeBee();
                return true;
            case R.id.action_profile:
                profile();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void writeBee() {
        Intent addBeeActivity = new Intent(MainActivity.this, AddBeeActivity.class);
        startActivity(addBeeActivity);
    }

    private void profile() {
        if (UserHandler.getUsername() == null) {
            Intent signInActivity = new Intent(MainActivity.this, BeeActivity.class);
            startActivity(signInActivity);
        }
    }





    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
        mSocket.off("beesList", onBeesList);
    }

}
