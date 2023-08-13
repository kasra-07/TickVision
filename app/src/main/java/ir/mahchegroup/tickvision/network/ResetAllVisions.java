package ir.mahchegroup.tickvision.network;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.mahchegroup.tickvision.classes.UserItems;
import ir.mahchegroup.tickvision.database.ModelVision;

public class ResetAllVisions {
    private final Context context;
    private final OnResetAllVisionsCallBack onResetAllVisionsCallBack;
    private static final String TAG = "ResetAllVisions";

    public ResetAllVisions(Context context) {
        this.context = context;
        onResetAllVisionsCallBack = (OnResetAllVisionsCallBack) context;
    }

    public void reset(String userTbl, String diff) {
        StringRequest request = new StringRequest(Request.Method.POST, UserItems.RESET_ALL_VISIONS_URL,
                response -> {
                    List<ModelVision> list = new ArrayList<>();
                    try {
                        JSONArray ja = new JSONArray(response);

                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jo = ja.getJSONObject(i);
                            ModelVision model = new ModelVision();
                            model.setId(jo.getInt("id"));
                            model.setTitle(jo.getString("title"));
                            model.setDate_vision(jo.getString("date_vision"));
                            model.setAmount(jo.getString("amount"));
                            model.setDay_vision(jo.getString("day_vision"));
                            model.setDay_amount(jo.getString("day_amount"));
                            model.setDay_pass(jo.getString("day_pass"));
                            model.setDay_rest(jo.getString("day_rest"));
                            model.setIncome_amount(jo.getString("income_amount"));
                            model.setRest_amount(jo.getString("rest_amount"));
                            model.setIncome(jo.getString("income"));
                            model.setPayment(jo.getString("payment"));
                            model.setProfit(jo.getString("profit"));
                            model.setRest(jo.getString("rest"));
                            model.setMilli_sec(jo.getString("milli_sec"));
                            model.setIs_tick(jo.getString("is_tick"));

                            list.add(model);
                        }

                    } catch (JSONException e) {
                        Log.e(TAG, "get | e : " + e);
                    }
                    onResetAllVisionsCallBack.onResetAllVisionsListener(list);
                },
                error -> Log.e(TAG, "reset | error : " + error))
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
        void onResetAllVisionsListener(List<ModelVision> resetAllList);
    }
}