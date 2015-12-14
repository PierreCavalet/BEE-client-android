package fr.pierrecavalet.bestexcuseever;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;

import fr.pierrecavalet.models.Bee;
import fr.pierrecavalet.sync.SocketHandler;
import fr.pierrecavalet.sync.UserHandler;

public class AddBeeActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private EditText mMessage;
    private EditText mLocation;
    private Socket mSocket;

    // gps
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private void attemptSend() throws JSONException {
        String message = mMessage.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            return;
        }
        Bee bee = new Bee(UserHandler.getUsername(), "No location yet", null, message, 0, 0);
        mMessage.setText("");
        mSocket.emit("sendBee", bee.toJSONObject());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bee);
        mSocket = SocketHandler.getSocket();
        // connection to google API
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mMessage = (EditText) findViewById(R.id.message);
        mLocation = (EditText) findViewById(R.id.location);

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

    // gps
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mLocation.setText("lat : " + String.valueOf(mLastLocation.getLatitude()) + "    long : " + String.valueOf(mLastLocation.getLongitude()));
            Log.d("latitude", String.valueOf(mLastLocation.getLatitude()));
            Log.d("longitude", String.valueOf(mLastLocation.getLongitude()));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
