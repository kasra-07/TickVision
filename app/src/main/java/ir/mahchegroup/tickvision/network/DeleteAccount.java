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

public class DeleteAccount {
    private final Context context;
    private final OnDeleteAccountCallBack onDeleteAccountCallBack;
    private static final String TAG = "DeleteAccount";

    public DeleteAccount(Context context) {
        this.context = context;
        onDeleteAccountCallBack = (OnDeleteAccountCallBack) context;
    }

    public void delete(String userMail, String userTbl) {
        StringRequest request = new StringRequest(Request.Method.POST, UserItems.DELETE_ACCOUNT_URL,
                onDeleteAccountCallBack::onDeleteAccountListener,
                error -> Log.e(TAG, "delete: " + error))
        {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put(UserItems.USER_MAIL, userMail);
                params.put(UserItems.USER_TBL, userTbl);
                return params;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }

    public interface OnDeleteAccountCallBack {
        void onDeleteAccountListener(String isDelete);
    }
}
