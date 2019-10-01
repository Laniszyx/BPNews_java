package com.example.navigationdrawerdemo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static com.example.navigationdrawerdemo.App.CHANNEL_1_ID;
import static com.example.navigationdrawerdemo.App.CHANNEL_2_ID;

public class PlayReceiver extends BroadcastReceiver {
    private int count;
    private Context context;
    //Notification
    private NotificationManagerCompat notificationManagerCompat;

    public PlayReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        //Notification
        createNotificationChannels();
        notificationManagerCompat= NotificationManagerCompat.from(this.context);

        sendOnChannel1();
        count++;

//        要執行的工作
//        用了一個 Bundle 類別接收廣播通知 intent，並判別傳進來的是否是要執行的通知，若是，則執行指定的工作。
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd H:mm:ss");
        String temp = dateFormat.format(new Date());
        Toast.makeText(context, count + ". onReceive.class :" + temp.toString(), Toast.LENGTH_SHORT).show();

    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("this is channel 1");

            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Channel 2",
                    NotificationManager.IMPORTANCE_LOW);
            channel2.setDescription("this is channel 2");

            NotificationManager manager = this.context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);

        }
    }


    public void sendOnChannel1() {
        Toast.makeText(context, "PlayReceiver.sendOnChannel1", Toast.LENGTH_SHORT).show();
        String title = "記得每天登入你的血壓記錄";
        String message = "量測血壓前記得先休息片刻，量測過程保持安靜並坐姿舒適端正";


        Intent intent = new Intent(this.context, RecordFragment.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.context, 0, intent, FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this.context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)//pendingintent是有條件才會啟動 pending是延遲的意思
                // setContentIntent只能放pendingintent
                //.setWhen(Calendar.getInstance().getTimeInMillis())
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true)
                .setShowWhen(true)
                .build();

        notificationManagerCompat.notify(1, notification);
    }
}
