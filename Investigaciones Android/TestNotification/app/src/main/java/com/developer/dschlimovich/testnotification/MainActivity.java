package com.developer.dschlimovich.testnotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button button;
    NotificationCompat.Builder notification;
    private static final int idUnique=694269;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);

        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                notification.setSmallIcon(R.mipmap.ic_launcher);

                notification.setPriority(NotificationCompat.PRIORITY_MAX);
                notification.setTicker("New nigga notification!"); // Efecto en el q te muestra un preview de la Notificacion
                notification.setWhen(System.currentTimeMillis());
                notification.setContentTitle("Erase una vez un negrito!");
                notification.setContentText("Watch out!! There is a short Negrito in the city");
                notification.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

                Intent intent = new Intent(MainActivity.this,MainActivity.class); //para que te abra un activity en particular

                PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                notification.setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(idUnique,notification.build());
            }
        });



    }
}
