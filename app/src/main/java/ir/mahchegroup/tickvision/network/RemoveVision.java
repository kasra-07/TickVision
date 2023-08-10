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

public class RemoveVision {
    private final Context context;
    private final OnRemoveVisionCallBack onRemoveVisionCallBack;
    private static final String TAG = "RemoveVision";

    public RemoveVision(Context context) {
        this.context = context;
        onRemoveVisionCallBack = (OnRemoveVisionCallBack) context;
    }

    public void remove(String userTbl, String title) {
        StringRequest request = new StringRequest(Request.Method.POST, UserItems.REMOVE_VISION_URL,
                response -> onRemoveVisionCallBack.onRemoveVisionListener(),
                error -> Log.e(TAG, "remove: " + error))
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

    public interface OnRemoveVisionCallBack {
        void onRemoveVisionListener();
    }
}
