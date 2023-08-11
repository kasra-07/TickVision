package ir.mahchegroup.tickvision.message_box;

import android.app.Dialog;
import android.content.Context;

public class UpdatePriceDialog {
    private Dialog dialog;
    private final Context context;

    public UpdatePriceDialog(Context context) {
        this.context = context;
    }

    public void show(boolean isIncome) {
        dialog = new Dialog(context);
    }
}
