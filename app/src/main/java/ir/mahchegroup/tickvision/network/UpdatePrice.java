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

public class UpdatePrice {
    private final Context context;
    private final OnUpdatePriceCallBack onUpdatePriceCallBack;
    private static final String TAG = "UpdatePrice";

    public UpdatePrice(Context context) {
        this.context = context;
        onUpdatePriceCallBack = (OnUpdatePriceCallBack) context;
    }

    public void set(String userTbl, String title, String incomeAmount, String restAmount, String income, String payment, String profit, String rest, String isTick) {
        StringRequest request = new StringRequest(Request.Method.POST, UserItems.UPDATE_PRICE_URL,
                onUpdatePriceCallBack::onUpdatePriceListener,
                error -> Log.e(TAG, "set: " + error))
        {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put(UserItems.USER_TBL, userTbl);
                params.put(UserItems.TITLE, title);
                params.put(UserItems.INCOME_AMOUNT, incomeAmount);
                params.put(UserItems.REST_AMOUNT, restAmount);
                params.put(UserItems.INCOME, income);
                params.put(UserItems.PAYMENT, payment);
                params.put(UserItems.PROFIT, profit);
                params.put(UserItems.REST, rest);
                params.put(UserItems.IS_TICK, isTick);
                return params;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }

    public interface OnUpdatePriceCallBack {
        void onUpdatePriceListener(String isUpdate);
    }
}
