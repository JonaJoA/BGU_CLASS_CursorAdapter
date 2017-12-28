package com.jonajoapps.sample;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.jonajoapps.caculatortest.R;

public class MyIntentService extends IntentService {


    public static final String FILE_DOWNLOAD_Q_ACTION = "FILE_DOWNLOAD_Q";
    public static final String FILE_DOWNLOAD_Q_FINISH = "FILE_DOWNLOAD_FINISH";
    public static final String CURRENT = "CURRENT";

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String param = intent.getStringExtra("PARAM");
        int index = 0;
        for (int i = 0; i < 10; i++) {
            Log.e("IntentService", param + " Start chunk " + index);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Intent br = new Intent(FILE_DOWNLOAD_Q_ACTION);
            br.putExtra(CURRENT, index);
            br.setPackage(getPackageName());
            sendBroadcast(br);

            Log.e("IntentService", "received chunk " + index);
            index++;
        }

        if (MainActivity.getmCurrentState() == MainActivity.ActivityState.START) {
            Intent br = new Intent(FILE_DOWNLOAD_Q_FINISH);
            br.setPackage(getPackageName());
            sendBroadcast(br);
        } else {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());

            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
            mBuilder.setContentTitle("My notification");
            mBuilder.setContentText("Hello World!");
            mBuilder.setVibrate(new long[]{1000, 2000, 3000, 4000});
            mBuilder.setTicker("This is ticker text");

            NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


// Builds the notification and issues it.
            mNotifyMgr.notify(1, mBuilder.build());
        }
    }
}