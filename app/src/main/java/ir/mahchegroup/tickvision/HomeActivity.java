package ir.mahchegroup.tickvision;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import ir.mahchegroup.tickvision.classes.Shared;
import ir.mahchegroup.tickvision.classes.UserItems;
import ir.mahchegroup.tickvision.database.ModelVision;
import ir.mahchegroup.tickvision.database.VisionDao;
import ir.mahchegroup.tickvision.database.VisionDatabase;
import ir.mahchegroup.tickvision.date.ChangeDate;
import ir.mahchegroup.tickvision.message_box.AddVisionDialog;
import ir.mahchegroup.tickvision.message_box.ClearAllVisionsDialog;
import ir.mahchegroup.tickvision.message_box.SelectVisionDialog;
import ir.mahchegroup.tickvision.message_box.ToastMessage;
import ir.mahchegroup.tickvision.message_box.UpdateAllVisionsDialog;
import ir.mahchegroup.tickvision.network.AddVision;
import ir.mahchegroup.tickvision.network.ClearAllVisions;
import ir.mahchegroup.tickvision.network.GetAllVisions;
import ir.mahchegroup.tickvision.network.GetCountVision;

public class HomeActivity extends AppCompatActivity implements GetCountVision.OnGetCountCallBack, UpdateAllVisionsDialog.OnUpdateAllVisionsDialogCallBack, ClearAllVisionsDialog.OnClearAllVisionsDialogCallBack, AddVisionDialog.OnAddVisionDialogCallBack, AddVision.OnAddVisionCallBack, ClearAllVisions.OnClearAllVisionsCallBack, GetAllVisions.OnGetAllVisionsCallBack, SelectVisionDialog.OnSelectVisionDialogCallBack {
    private RelativeLayout btnAddVisionRoot, btnSelectVisionRoot;
    private LinearLayout homeToolbarRoot, btnAddLayout, btnSelectLayout;
    private Toolbar toolbar;
    private TextView tvToolbar, tvTime, tvTimer, tvDay, tvDate, tvTitleTimer, tvIncome, tvPayment, tvProfit, tvRest, titleIncome, titlePayment, titleProfit, titleRest;
    private ImageView imgToolbar, btnAddDescription, btnSelectDescription, timerSwitch;
    private FloatingActionButton btnAdd, btnSelect;
    private DrawerLayout drawer;
    private CardView tableLayout, timeLayout, incomeLayout, paymentLayout;
    private View tableView, timeView, incomeView, paymentView, dimMenu;
    private FloatingActionMenu menu;
    private Shared shared;
    private VisionDao dao;
    private int onLineCount, ofLineCount;
    private boolean isFirstTime, isUserAddVision, isUserSelectVision;
    public static boolean isCheckDayMode = true;
    private String userTbl, date, title, amount, day;
    private UpdateAllVisionsDialog updateAllVisionsDialog;
    private ClearAllVisionsDialog clearAllVisionsDialog;
    private ClearAllVisions clearAllVisions;
    private AddVisionDialog addVisionDialog;
    private AddVision addVision;
    private GetAllVisions getAllVisions;
    private SelectVisionDialog selectVisionDialog;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

        if (isFirstTime) {
            GetCountVision getCountVision = new GetCountVision(this);
            getCountVision.getCount(userTbl);
        } else {
            if (!isUserAddVision || !isUserSelectVision) {
                setOnBtnAddClickListener();
                setOnBtnSelectClickListener();

            } else {
                startNormallyActivity();
            }
        }
    }

    private void setOnBtnAddClickListener() {
        btnAddVisionRoot.setVisibility(View.VISIBLE);
        btnAdd.setOnClickListener(v -> addVisionDialog.show());
    }

    private void setOnBtnSelectClickListener() {
        btnSelectVisionRoot.setVisibility(View.VISIBLE);
        btnSelect.setOnClickListener(v -> selectVisionDialog.show());
    }

    private void startNormallyActivity() {
        btnAddVisionRoot.setVisibility(View.GONE);
        btnSelectVisionRoot.setVisibility(View.GONE);

        initTableViewAndTimeView();
    }

    @SuppressLint("InflateParams")
    private void initTableViewAndTimeView() {
        timeLayout.removeAllViews();
        tableLayout.removeAllViews();

        timeView = inflater.inflate(R.layout.time_layout, null);
        tableView = inflater.inflate(R.layout.table_layout, null);

        timeLayout.addView(timeView);
        tableLayout.addView(tableView);

        initTimeAndTableChildren();
    }

    private void initTimeAndTableChildren() {
        tvTime = timeView.findViewById(R.id.tv_time);
        tvTimer = timeView.findViewById(R.id.tv_timer);
        tvDay = timeView.findViewById(R.id.tv_day);
        tvDate = timeView.findViewById(R.id.tv_date);
        tvTitleTimer = timeView.findViewById(R.id.tv_title_timer);

        tvIncome = tableView.findViewById(R.id.tv_income);
        titleIncome = tableView.findViewById(R.id.title_income);
        tvPayment = tableView.findViewById(R.id.tv_payment);
        titlePayment = tableView.findViewById(R.id.title_payment);
        tvProfit = tableView.findViewById(R.id.tv_profit);
        titleProfit = tableView.findViewById(R.id.title_profit);
        tvRest = tableView.findViewById(R.id.tv_rest);
        titleRest = tableView.findViewById(R.id.title_rest);
    }

    @Override
    public void onGetCountListener(int count) {
        ofLineCount = dao.getCountVision();
        onLineCount = count;
        if (onLineCount > ofLineCount) {
            updateAllVisionsDialog.show();
        }
    }

    @Override
    public void onUpdateAllVisionsDialogUpdateListener() {
        getAllVisions.get(userTbl);
    }

    @Override
    public void onUpdateAllVisionsDialogRemoveListener() {
        updateAllVisionsDialog.dismiss();
        clearAllVisionsDialog.show();
    }

    @Override
    public void onClearAllVisionsDialogRemoveListener() {
        clearAllVisions.clear(userTbl);
    }

    @Override
    public void onClearAllVisionsDialogBackListener() {
        clearAllVisionsDialog.dismiss();
        updateAllVisionsDialog.show();
    }

    @Override
    public void onAddVisionDialogSaveListener(String title, String amount, String day) {
        date = ChangeDate.getCurrentDay() + "/" + ChangeDate.getCurrentMonth() + "/" + ChangeDate.getCurrentYear();
        this.title = title;
        this.amount = amount;
        this.day = day;

        addVision.add(userTbl, title, amount, day, date);
    }

    @Override
    public void onAddVisionDialogCancelListener() {
        addVisionDialog.dismiss();

        if (!isUserAddVision) {
            setOnBtnAddClickListener();

        } else if (!isUserSelectVision) {
            setOnBtnAddClickListener();
            setOnBtnSelectClickListener();

        } else {
            startNormallyActivity();
        }
    }

    @Override
    public void onAddVisionListener(String isAddVision) {
        if (isAddVision.equals("success")) {
            String dayAmount = String.valueOf(Integer.parseInt(amount) / Integer.parseInt(day));
            ModelVision model = new ModelVision();
            model.setTitle(title);
            model.setDate_vision(date);
            model.setAmount(amount);
            model.setDay_vision(day);
            model.setDay_amount(dayAmount);
            model.setDay_pass("0");
            model.setDay_rest(day);
            model.setIncome_amount("0");
            model.setRest_amount(amount);
            model.setIncome("0");
            model.setPayment("0");
            model.setProfit("0");
            model.setRest(dayAmount);
            model.setMilli_sec("0");
            model.setIs_tick("0");

            dao.addVision(model);
            addVisionDialog.dismiss();
            ToastMessage.show(this, getString(R.string.add_vision_success), true, true);

            if (!isUserAddVision) {
                shared.getEditor().putBoolean(UserItems.IS_USER_ADD_VISION, true).apply();
                setOnBtnSelectClickListener();

            } else if (!isUserSelectVision) {
                selectVisionDialog.show();

            } else {
                startNormallyActivity();
            }

        } else if (isAddVision.equals("duplicate")) {
            ToastMessage.show(this, getString(R.string.add_vision_duplicate_error), false, true);
        } else {
            ToastMessage.show(this, getString(R.string.add_vision_error), false, true);
        }
    }

    @Override
    public void onClearAllVisionsListener(String isClearAllVisions) {
        if (isClearAllVisions.equals("success")) {
            clearAllVisionsDialog.dismiss();
            ToastMessage.show(this, getString(R.string.clear_all_vision_success_text), true, true);
            setOnBtnAddClickListener();
            btnSelectVisionRoot.setVisibility(View.GONE);
            shared.getEditor().putBoolean(UserItems.IS_FIRST_TIME, false).apply();
        } else {
            ToastMessage.show(this, getString(R.string.clear_all_visions_error), false, false);
        }
    }

    @Override
    public void onGetAllVisionsListener(List<ModelVision> getAllVisionsList) {
        if (!isUserSelectVision) {
            for (int i = 0; i < getAllVisionsList.size(); i++) {
                dao.addVision(getAllVisionsList.get(i));
            }
            updateAllVisionsDialog.dismiss();
            ToastMessage.show(this, getString(R.string.update_all_visions_success_text), true, true);
            setOnBtnAddClickListener();
            setOnBtnSelectClickListener();
            shared.getEditor().putBoolean(UserItems.IS_FIRST_TIME, false).apply();
        }
    }

    @Override
    public void onSelectVisionDialogSelectListener(String titleSelectVision) {

    }

    @Override
    public void onSelectVisionDialogCancelListener() {
        selectVisionDialog.dismiss();
        if (!isUserSelectVision) {
            setOnBtnAddClickListener();
            setOnBtnSelectClickListener();
        }
    }

    private void init() {
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        btnAddVisionRoot = findViewById(R.id.btn_add_root);
        btnSelectVisionRoot = findViewById(R.id.btn_select_root);
        btnAddLayout = findViewById(R.id.btn_add_layout);
        btnSelectLayout = findViewById(R.id.btn_select_layout);
        toolbar = findViewById(R.id.toolbar);
        tvToolbar = findViewById(R.id.tv_toolbar);
        imgToolbar = findViewById(R.id.img_toolbar);
        btnAddDescription = findViewById(R.id.btn_add_description);
        btnSelectDescription = findViewById(R.id.btn_select_description);
        timerSwitch = findViewById(R.id.timer_switch);
        btnAdd = findViewById(R.id.btn_add);
        btnSelect = findViewById(R.id.btn_select);
        drawer = findViewById(R.id.drawer);
        dimMenu = findViewById(R.id.dim_menu);
        tableLayout = findViewById(R.id.table_layout);
        timeLayout = findViewById(R.id.time_layout);
        incomeLayout = findViewById(R.id.income_layout);
        paymentLayout = findViewById(R.id.payment_layout);
        menu = findViewById(R.id.menu);
        shared = new Shared(this);
        isFirstTime = shared.getShared().getBoolean(UserItems.IS_FIRST_TIME, true);
        isUserAddVision = shared.getShared().getBoolean(UserItems.IS_USER_ADD_VISION, false);
        isUserSelectVision = shared.getShared().getBoolean(UserItems.IS_USER_SELECT_VISION, false);
        userTbl = shared.getShared().getString(UserItems.USER_TBL, "");
        dao = VisionDatabase.getVisionDatabase(this).visionDao();

        updateAllVisionsDialog = new UpdateAllVisionsDialog(this);
        clearAllVisionsDialog = new ClearAllVisionsDialog(this);
        clearAllVisions = new ClearAllVisions(this);
        addVisionDialog = new AddVisionDialog(this);
        addVision = new AddVision(this);
        getAllVisions = new GetAllVisions(this);
        selectVisionDialog = new SelectVisionDialog(this);
    }
}