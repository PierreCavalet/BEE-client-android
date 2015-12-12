package fr.pierrecavalet.bestexcuseever;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;

import fr.pierrecavalet.models.Bee;
import fr.pierrecavalet.sync.SocketHandler;
import fr.pierrecavalet.sync.UserHandler;

public class AddBeeActivity extends AppCompatActivity {

    private EditText mInputMessageView;
    private Socket mSocket;
    private LocationManager locationManager;
    private double latitude;
    private double longitude;

    private void attemptSend() throws JSONException {
        String message = mInputMessageView.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            return;
        }
        Bee bee = new Bee(UserHandler.getUsername(), "No location yet", null, message, 0, 0);
        mInputMessageView.setText("");
        mSocket.emit("sendBee", bee.toJSONObject());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bee);
        mSocket = SocketHandler.getSocket();


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 150, new LocationListener() {

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        });



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
