package fr.pierrecavalet.bestexcuseever.services;

/**
 * Created by pierre on 26/12/15.
 */
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import fr.pierrecavalet.bestexcuseever.BestExcuseEverPreferences;
import fr.pierrecavalet.bestexcuseever.R;
import fr.pierrecavalet.bestexcuseever.sync.SocketHandler;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};
    private Socket mSocket;

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSocket = SocketHandler.getSocket();

        try {
            // R.string.gcm_defaultSenderId (the Sender ID) is derived from google-services.json
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Log.i(TAG, "GCM Registration Token: " + token);

            // send the token to the server
            sendRegistrationToServer(token);

            // If the boolean is false, send the token to the server,
            // otherwise the server should have already received the token
            sharedPreferences.edit()
                    .putBoolean(BestExcuseEverPreferences.SENT_TOKEN_TO_SERVER, true).apply();
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            sharedPreferences.edit()
                    .putBoolean(BestExcuseEverPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden
        Intent registrationComplete = new Intent(BestExcuseEverPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        mSocket.emit("enableNotifications", token);
    }

}
