package ir.mahchegroup.tickvision.classes;

import android.annotation.SuppressLint;
import android.app.Application;
import android.icu.text.SimpleDateFormat;

import ir.mahchegroup.tickvision.date.ChangeDate;

public class G extends Application {
    private Shared shared;

    @Override
    public void onCreate() {
        super.onCreate();

        shared = new Shared(this);
        int gDay = ChangeDate.getCurrentDay();
        boolean isGetDay = shared.getShared().getBoolean(UserItems.IS_GET_DAY, false);

        isChangeDay(isGetDay, gDay);
    }

    private void isChangeDay(boolean isGetDay, int gDay) {
        if (!isGetDay) {
            shared.getEditor().putInt(UserItems.G_DAY, gDay);
            shared.getEditor().putBoolean(UserItems.IS_GET_DAY, true);
            shared.getEditor().putBoolean(UserItems.IS_CHANGE_DAY, false);
            shared.getEditor().apply();
        } else {
            if (shared.getShared().getInt(UserItems.G_DAY, 0) != gDay) {
                shared.getEditor().putBoolean(UserItems.IS_CHANGE_DAY, true);
                shared.getEditor().putBoolean(UserItems.IS_GET_DAY, false);

                long diff = calcTimeDiff(String.valueOf(shared.getShared().getInt(UserItems.G_DAY, 0)), String.valueOf(gDay));
                shared.getEditor().putString(UserItems.DIFF, String.valueOf(diff));

                shared.getEditor().apply();
            } else {
                shared.getEditor().putBoolean(UserItems.IS_CHANGE_DAY, false);
                shared.getEditor().apply();
            }
        }
    }


    @SuppressLint("SimpleDateFormat")
    public long calcTimeDiff(String dateStart, String dateStop) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

        java.util.Date d1;
        java.util.Date d2;

        long diffDays = 0;

        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);

            long diff = d2.getTime() - d1.getTime();

            diffDays = diff / (24 * 60 * 60 * 1000);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return diffDays;
    }
}
