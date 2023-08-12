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

public class ResetAllVisions {
    private OnResetAllVisionsCallBack onResetAllVisionsCallBack;
    private static final String TAG = "ResetAllVisions";

    public void setOnResetAllVisionsCallBack(OnResetAllVisionsCallBack onResetAllVisionsCallBack) {
        this.onResetAllVisionsCallBack = onResetAllVisionsCallBack;
    }

    public void reset(Context context, String userTbl, String diff) {
        StringRequest request = new StringRequest(Request.Method.POST, UserItems.RESET_ALL_VISIONS_URL,
                response -> onResetAllVisionsCallBack.onResetAllVisionListener(response),
                error -> Log.e(TAG, "reset: " + error))
        {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put(UserItems.USER_TBL, userTbl);
                params.put(UserItems.DIFF, diff);
                return params;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }

    public interface OnResetAllVisionsCallBack {
        void onResetAllVisionListener(String isReset);
    }
}
