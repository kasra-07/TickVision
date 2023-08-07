package ir.mahchegroup.tickvision.classes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckConnection {
    public static boolean check(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        return info != null && info.isConnectedOrConnecting();
    }
}
