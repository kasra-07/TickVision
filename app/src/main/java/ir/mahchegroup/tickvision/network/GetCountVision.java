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

public class GetCountVision {
    private final Context context;
    private final OnGetCountCallBack onGetCountCallBack;
    private static final String TAG = "GetCountVision";

    public GetCountVision(Context context) {
        this.context = context;
        onGetCountCallBack = (OnGetCountCallBack) context;
    }

    public void getCount(String userTbl) {
        StringRequest request = new StringRequest(Request.Method.POST, UserItems.GET_COUNT_VISION_URL,
                response -> onGetCountCallBack.onGetCountListener(Integer.parseInt(response)),
                error -> Log.e(TAG, "getCount: "+error))
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

    public interface OnGetCountCallBack {
        void onGetCountListener(int count);
    }
}
