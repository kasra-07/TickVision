package ir.mahchegroup.tickvision.message_box;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ir.mahchegroup.tickvision.HomeActivity;
import ir.mahchegroup.tickvision.R;
import ir.mahchegroup.tickvision.database.ModelVision;
import ir.mahchegroup.tickvision.database.VisionDao;
import ir.mahchegroup.tickvision.database.VisionDatabase;
import ir.mahchegroup.tickvision.rv.RecyclerItemClick;
import ir.mahchegroup.tickvision.rv.SelectRvAdapter;
import ir.mahchegroup.tickvision.rv.SelectRvModel;

public class SelectVisionDialog {
    private final Context context;
    private final OnSelectVisionDialogCallBack onSelectVisionDialogCallBack;
    private Dialog dialog;
    private final VisionDao visionDao;
    private List<ModelVision> list;
    private String selectedVision;

    public SelectVisionDialog(Context context) {
        this.context = context;
        onSelectVisionDialogCallBack = (OnSelectVisionDialogCallBack) context;
        visionDao = VisionDatabase.getVisionDatabase(context).visionDao();
    }

    @SuppressLint("InflateParams")
    public void show() {
        dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.select_vision_dialog_layout, null);
        TextView tv = view.findViewById(R.id.tv_text_select_vision_dialog);
        TextView title = view.findViewById(R.id.tv_title_dialog);

        tv.setText(HomeActivity.isCheckDayMode ? context.getString(R.string.select_vision_day_dialog_text) : context.getString(R.string.select_vision_edit_dialog_text));
        title.setText(HomeActivity.isCheckDayMode ? context.getString(R.string.select_vision_day_dialog_title) : context.getString(R.string.select_vision_edit_dialog_title));

        RecyclerView rv = view.findViewById(R.id.select_dialog_rv);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnSelect = view.findViewById(R.id.btn_select);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.dialog_anim;

        list = visionDao.getAllVisions();

        List<SelectRvModel> selectList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            SelectRvModel model = new SelectRvModel(list.get(i).getTitle(), list.get(i).getIs_tick());
            selectList.add(model);
        }

        rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        SelectRvAdapter adapter = new SelectRvAdapter(context, selectList);
        rv.setAdapter(adapter);

        rv.addOnItemTouchListener(new RecyclerItemClick(dialog.getContext(), (view12, position) -> {
            selectedVision = list.get(position).getTitle();
            tv.setText(selectedVision);
        }));

        btnCancel.setOnClickListener(v -> onSelectVisionDialogCallBack.onSelectVisionDialogCancelListener());

        btnSelect.setOnClickListener(v -> {
            if (tv.getText().toString().equals(context.getString(R.string.select_vision_day_dialog_text)) || tv.getText().toString().equals(context.getString(R.string.select_vision_edit_dialog_text))) {
                ToastMessage.show(dialog.getContext(), context.getString(R.string.select_vision_error), false, true);
            } else {
                onSelectVisionDialogCallBack.onSelectVisionDialogSelectListener(tv.getText().toString());
            }
        });

        dialog.setContentView(view);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        dialog.create();
        dialog.show();

    }

    public void dismiss() {
        dialog.dismiss();
    }

    public interface OnSelectVisionDialogCallBack {
        void onSelectVisionDialogSelectListener(String titleSelectVision);

        void onSelectVisionDialogCancelListener();
    }
}
