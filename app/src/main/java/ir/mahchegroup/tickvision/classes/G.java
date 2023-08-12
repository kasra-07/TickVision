package ir.mahchegroup.tickvision.classes;

import android.app.Application;

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
                shared.getEditor().apply();
            } else {
                shared.getEditor().putBoolean(UserItems.IS_CHANGE_DAY, false);
                shared.getEditor().apply();
            }
        }
    }
}
