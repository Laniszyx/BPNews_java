package com.example.navigationdrawerdemo;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static com.example.navigationdrawerdemo.App.CHANNEL_1_ID;
import static com.example.navigationdrawerdemo.App.CHANNEL_2_ID;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    //notify
    private NotificationManagerCompat notificationManagerCompat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Notification
        createNotificationChannels();
        notificationManagerCompat = NotificationManagerCompat.from(this);
//        sendOnChannel1();//這個放進去的話 僅開啟APP可以產生提醒
//        onAlarmSetup();
        //check_products();


        Toolbar toolbar = findViewById(R.id.toolbar);//以下兩行跑出左側toolbar
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawerlayout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);//使用this的話要implement並複寫方法

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer,
                    new NewsFragment()).commit();//讓啟動程式得時候不會nullpoint 要先有個畫面
            navigationView.setCheckedItem(R.id.nav_news);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_record:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer,
                        new RecordFragment()).commit();
                break;
            case R.id.nav_news:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer,
                        new NewsFragment()).commit();
                break;
            case R.id.nav_chart:
                Intent intent = new Intent(this, ActivityB.class);
                startActivity(intent);
                break;
            default:
                Toast.makeText(this, "Default", Toast.LENGTH_LONG).show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);//點選完左側要關起來
        return true;//false表示無item
    }

    @Override
    public void onBackPressed() {//
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);

        }
    }


//    public void sendOnChannel1() {
//        String title = "記得每天登入你的血壓記錄";
//        String message = "量測血壓前記得先休息片刻，量測過程保持安靜並坐姿舒適端正";
//
//
//        Intent intent = new Intent(this, RecordFragment.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//
//        Notification notification = new NotificationCompat.Builder(this.getBaseContext(), CHANNEL_1_ID)
//                .setSmallIcon(R.drawable.ic_one)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setContentIntent(pendingIntent)//pendingintent是有條件才會啟動 pending是延遲的意思
//                // setContentIntent只能放pendingintent
//                //.setWhen(Calendar.getInstance().getTimeInMillis())
//                .setWhen(System.currentTimeMillis())
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                .setShowWhen(true)
//                .build();
//
//        notificationManagerCompat.notify(1, notification);
//    }

//    public void onAlarmSetup() {
//        Toast.makeText(this.getBaseContext(), "onAlarmSetup", Toast.LENGTH_LONG).show();
//
//        Calendar cal = Calendar.getInstance();
//        cal.setTimeInMillis(System.currentTimeMillis());
////        cal.add(Calendar.SECOND, 10);
//        //cal.set(Calendar.HOUR_OF_DAY,  );//可設定時間
//
//        Intent intent = new Intent(this.getBaseContext(), PlayReceiver.class);
//        PendingIntent pi = PendingIntent.getBroadcast(this.getBaseContext(), 0, intent, FLAG_UPDATE_CURRENT);
//
//        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 1000 * 60, pi);//每60秒叫一次
//        //am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pi);
//        //am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);//
//    }

}

