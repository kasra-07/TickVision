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

public class UserSignup {
    private final Context context;
    private final UserSignupCallBack userSignupCallBack;
    private static final String TAG = "UserSignup";

    public UserSignup(Context context) {
        this.context = context;
        userSignupCallBack = (UserSignupCallBack) context;
    }

    public void signup(String username, String mail, String pass) {
        StringRequest request = new StringRequest(Request.Method.POST, UserItems.USER_SIGNUP_URL,
                userSignupCallBack::onUserSignupListener,
                error -> Log.e(TAG, "signup: " + error))
        {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put(UserItems.USERNAME, username);
                params.put(UserItems.MAIL, mail);
                params.put(UserItems.PASS, pass);
                return params;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }

    public interface UserSignupCallBack {
        void onUserSignupListener(String isSignup);
    }
}
