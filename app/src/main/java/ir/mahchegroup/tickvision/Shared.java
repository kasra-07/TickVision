package ir.mahchegroup.tickvision;

import android.content.Context;
import android.content.SharedPreferences;

public class Shared {
    private final SharedPreferences shared;
    private final SharedPreferences.Editor editor;

    public Shared(Context context) {
        shared = context.getSharedPreferences("shared", Context.MODE_PRIVATE);
        editor = shared.edit();
    }

    public SharedPreferences getShared() {
        return shared;
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }
}
