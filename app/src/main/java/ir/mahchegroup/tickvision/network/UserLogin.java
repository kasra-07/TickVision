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

public class UserLogin {
    private final Context context;
    private final OnUserLoginCallBack onUserLoginCallBack;
    private static final String TAG = "UserLogin";

    public UserLogin(Context context) {
        this.context = context;
        onUserLoginCallBack = (OnUserLoginCallBack) context;
    }

    public void login(String userMail, String pass) {
        StringRequest request = new StringRequest(Request.Method.POST, UserItems.USER_LOGIN_URL,
                response -> {
                    List<String> list = new ArrayList<>();
                    try {
                        JSONObject jo = new JSONObject(response);
                        String status = jo.getString("status");
                        String username = jo.getString("username");
                        String mail = jo.getString("mail");
                        String userTbl = jo.getString("user_tbl");

                        list.add(status);
                        list.add(username);
                        list.add(mail);
                        list.add(userTbl);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "login | e : " + e);
                    }
                    onUserLoginCallBack.onUserLoginListener(list);
                },
                error -> Log.e(TAG, "login | error : " + error))
        {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put(UserItems.USER_MAIL, userMail);
                params.put(UserItems.PASS, pass);
                return params;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }

    public interface OnUserLoginCallBack {
        void onUserLoginListener(List<String> loginList);
    }
}
