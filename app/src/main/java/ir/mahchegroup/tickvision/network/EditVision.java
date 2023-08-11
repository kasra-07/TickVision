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

public class EditVision {
    private final Context context;
    private final OnEditVisionCallBack onEditVisionCallBack;
    private static final String TAG = "EditVision";

    public EditVision(Context context) {
        this.context = context;
        onEditVisionCallBack = (OnEditVisionCallBack) context;
    }

    public void edit(String userTbl, String title, String newTitle, String dateVision, String amount, String dayVision, String dayAmount) {
        StringRequest request = new StringRequest(Request.Method.POST, UserItems.EDIT_VISION_URL,
                onEditVisionCallBack::onEditVisionListener,
                error -> Log.e(TAG, "edit: " + error))
        {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put(UserItems.USER_TBL, userTbl);
                params.put(UserItems.TITLE, title);
                params.put(UserItems.NEW_TITLE, newTitle);
                params.put(UserItems.DATE_VISION, dateVision);
                params.put(UserItems.AMOUNT, amount);
                params.put(UserItems.DAY_VISION, dayVision);
                params.put(UserItems.DAY_AMOUNT, dayAmount);
                return params;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }

    public interface OnEditVisionCallBack {
        void onEditVisionListener(String isEditVision);
    }
}
