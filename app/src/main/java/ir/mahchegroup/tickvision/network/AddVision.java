package ir.mahchegroup.tickvision.network;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.mahchegroup.tickvision.classes.UserItems;

public class AddVision {
    private final Context context;
    private final OnAddVisionCallBack onAddVisionCallBack;
    private static final String TAG = "AddVision";

    public AddVision(Context context) {
        this.context = context;
        onAddVisionCallBack = (OnAddVisionCallBack) context;
    }

    public void add(String userTbl, String title, String amount, String dayVision, String dateVision) {
        StringRequest request = new StringRequest(Request.Method.POST, UserItems.ADD_VISION_URL,
                response -> {
                    Map<String,String> list = new HashMap<>();
                    try {
                        JSONObject jo = new JSONObject(response);
                        String status = jo.getString("status");
                        String id = jo.getString("id");
                        list.put("status", status);
                        list.put("id", id);
                    } catch (JSONException e) {
                        Log.e(TAG, "add | e : " + e);
                    }
                    onAddVisionCallBack.onAddVisionListener(list);
                },
                error -> Log.e(TAG, "add | error : " + error)) {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(UserItems.USER_TBL, userTbl);
                params.put(UserItems.TITLE, title);
                params.put(UserItems.AMOUNT, amount);
                params.put(UserItems.DAY_VISION, dayVision);
                params.put(UserItems.DATE_VISION, dateVision);
                return params;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }

    public interface OnAddVisionCallBack {
        void onAddVisionListener(Map<String,String> addVisionList);
    }
}
