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

public class ClearAllVisions {
    private final Context context;
    private final OnClearAllVisionsCallBack onClearAllVisionsCallBack;
    private static final String TAG = "ClearAllVisions";

    public ClearAllVisions(Context context) {
        this.context = context;
        onClearAllVisionsCallBack = (OnClearAllVisionsCallBack) context;
    }

    public void clear(String userTbl) {
        StringRequest request = new StringRequest(Request.Method.POST, UserItems.CLEAR_ALL_VISIONS_URL,
                response -> onClearAllVisionsCallBack.onClearAllVisionsListener("success"),
                error -> {
                    Log.e(TAG, "clear: " + error);
                    onClearAllVisionsCallBack.onClearAllVisionsListener("error");
                })
        {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put(UserItems.USER_TBL, userTbl);
                return params;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }

    public interface OnClearAllVisionsCallBack {
        void onClearAllVisionsListener(String isClearAllVisions);
    }
}
