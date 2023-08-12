package ir.mahchegroup.tickvision.network;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import ir.mahchegroup.tickvision.classes.UserItems;

public class UpdateMilliSec {
    private final Context context;
    private static final String TAG = "UpdateMilliSec";

    public UpdateMilliSec(Context context) {
        this.context = context;
    }

    public void set(String userTbl, String title, String milliSec) {
        StringRequest request = new StringRequest(Request.Method.POST, UserItems.UPDATE_MILLI_SEC_URL,
                response -> {
                },
                error -> Log.e(TAG, "set: " + error))
        {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put(UserItems.USER_TBL, userTbl);
                params.put(UserItems.TITLE, title);
                params.put(UserItems.MILLI_SEC, milliSec);
                return params;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }
}
