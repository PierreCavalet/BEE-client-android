package fr.pierrecavalet.bestexcuseever.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;

import fr.pierrecavalet.bestexcuseever.Constants;
import fr.pierrecavalet.bestexcuseever.FetchAddressIntentService;
import fr.pierrecavalet.bestexcuseever.R;
import fr.pierrecavalet.bestexcuseever.models.Bee;
import fr.pierrecavalet.bestexcuseever.sync.SocketHandler;
import fr.pierrecavalet.bestexcuseever.sync.UserHandler;

public class AddBeeActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private EditText mMessage;
    private EditText mLocation;
    private Socket mSocket;
    private AddressResultReceiver mResultReceiver;

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string
            // or an error message sent from the intent service.
            mLocation.setText(resultData.getString(Constants.RESULT_DATA_KEY));

            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                showToast(getString(R.string.address_found));
            }

        }
    }

    private void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

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
            Log.d("latitude", String.valueOf(mLastLocation.getLatitude()));
            Log.d("longitude", String.valueOf(mLastLocation.getLongitude()));
            Intent intent = new Intent(this, FetchAddressIntentService.class);
            mResultReceiver = new AddressResultReceiver(new Handler());
            intent.putExtra(Constants.RECEIVER, mResultReceiver);
            intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
            startService(intent);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
