package ir.mahchegroup.tickvision.network;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ir.mahchegroup.tickvision.Animations;
import ir.mahchegroup.tickvision.CheckConnection;
import ir.mahchegroup.tickvision.UserItems;
import ir.mahchegroup.tickvision.activities.DisconnectActivity;

public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!isConnected(context)) {
            intent = new Intent(context, DisconnectActivity.class);
            String activityName = context.getClass().getSimpleName();
            intent.putExtra(UserItems.ACTIVITY_NAME, activityName);
            Animations.AnimActivity(context, intent);
            ((Activity)context).finish();
        }
    }

    private boolean isConnected(Context context) {
        return CheckConnection.check(context);
    }
}
