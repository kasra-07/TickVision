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

public class ResetVision {
    private final Context context;
    private final OnResetVisionCallBack onResetVisionCallBack;
    private static final String TAG = "ResetVision";

    public ResetVision(Context context) {
        this.context = context;
        onResetVisionCallBack = (OnResetVisionCallBack) context;
    }

    public void reset(String userTbl, String title) {
        StringRequest request = new StringRequest(Request.Method.POST, UserItems.RESET_VISION_URL,
                onResetVisionCallBack::onResetVisionListener,
                error -> Log.e(TAG, "reset: " + error))
        {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put(UserItems.USER_TBL, userTbl);
                params.put(UserItems.TITLE, title);
                return params;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }

    public interface OnResetVisionCallBack {
        void onResetVisionListener(String isReset);
    }
}
