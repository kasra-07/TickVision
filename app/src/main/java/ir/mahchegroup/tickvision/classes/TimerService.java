package ir.mahchegroup.tickvision.classes;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import java.util.Locale;

import ir.mahchegroup.tickvision.HomeActivity;
import ir.mahchegroup.tickvision.R;

public class TimerService extends Service {
    private static final String NOTIFY_CHANNEL = "Default";
    private static final int NOTIFY_ID = 1;
    private static NotificationManager manager;
    public UpdateUiCallBack updateUiCallBack;
    public static boolean RUNNING = false;
    private final TimeBinder timeBinder = new TimeBinder();
    private int a = HomeActivity.MILLI_SEC;
    public static int COUNTER;

    public void setUpdateUiCallBack(UpdateUiCallBack updateUiCallBack) {
        this.updateUiCallBack = updateUiCallBack;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return timeBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFY_CHANNEL, "it", NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(notificationChannel);
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Notification notification = new NotificationCompat.Builder(this, NOTIFY_CHANNEL)
                .setContentTitle(getResources().getString(R.string.tick____vision))
                .setContentText(getResources().getString(R.string.notify_text))
                .setColor(Color.argb(255, 130, 230, 190))
                .setSmallIcon(R.drawable.vision)
                .build();
        startForeground(NOTIFY_ID, notification);

        RUNNING = true;

        Thread thread = new Thread(this::setTimer);
        thread.start();
        return START_NOT_STICKY;
    }

    private void updateNotify(String time) {
        Notification notification = new NotificationCompat.Builder(this, NOTIFY_CHANNEL)
                .setContentTitle(getResources().getString(R.string.notify_text))
                .setContentText(getResources().getString(R.string.working_hours_day_text) + time)
                .setColor(Color.argb(255, 130, 230, 190))
                .setSmallIcon(R.drawable.vision)
                .build();
        manager.notify(NOTIFY_ID, notification);
    }

    private String timeFormat(long t) {
        int sec = (int) (t / 1000);
        int min = sec / 60;
        int hour = min / 60;
        sec %= 60;
        min %= 60;
        hour %= 24;
        return FaNum.convert(String.format(Locale.ENGLISH, "%02d", sec) + " : " + String.format(Locale.ENGLISH, "%02d", min) + " : " + String.format(Locale.ENGLISH, "%02d", hour));
    }

    public void setTimer() {
        if (updateUiCallBack != null) {
            a += 1000;
            COUNTER = a;
            updateUiCallBack.updateUi(timeFormat(a));
            updateNotify(timeFormat(a));
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (RUNNING) {
            setTimer();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RUNNING = false;
        stopForeground(true);
        stopSelf();
    }

    public class TimeBinder extends Binder {
        public TimerService getService() {
            return TimerService.this;
        }
    }

    public interface UpdateUiCallBack {
        void updateUi(String time);
    }
}