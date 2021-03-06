package fr.pierrecavalet.bestexcuseever.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import fr.pierrecavalet.bestexcuseever.R;
import fr.pierrecavalet.bestexcuseever.activities.MainActivity;

/**
 * Created by pierre on 28/12/15.
 */
public class BeeGcmListenerService extends GcmListenerService {

    private static final String TAG = "BeeGcmListenerService";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        sendNotification(getString(R.string.new_bee_notification_content));
    }

    private void sendNotification(String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.couronne_violette)
                .setContentTitle(getString(R.string.new_bee_notification_title))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
