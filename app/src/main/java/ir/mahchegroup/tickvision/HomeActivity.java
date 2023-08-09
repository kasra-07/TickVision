package ir.mahchegroup.tickvision;

import androidx.appcompat.app.ActionBarDrawerToggle;
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
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import ir.mahchegroup.tickvision.classes.FaNum;
import ir.mahchegroup.tickvision.classes.Shared;
import ir.mahchegroup.tickvision.classes.UserItems;
import ir.mahchegroup.tickvision.database.ModelVision;
import ir.mahchegroup.tickvision.database.VisionDao;
import ir.mahchegroup.tickvision.database.VisionDatabase;
import ir.mahchegroup.tickvision.date.ChangeDate;
import ir.mahchegroup.tickvision.date.ShamsiCalendar;
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
    private LayoutInflater inflater;
    public static boolean isCheckDayMode = true;
    private String userTbl, date, title, amount, day, selected_vision;
    private ModelVision selectVisionModel;
    private UpdateAllVisionsDialog updateAllVisionsDialog;
    private ClearAllVisionsDialog clearAllVisionsDialog;
    private ClearAllVisions clearAllVisions;
    private AddVisionDialog addVisionDialog;
    private AddVision addVision;
    private GetAllVisions getAllVisions;
    private SelectVisionDialog selectVisionDialog;

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
        drawer.setVisibility(View.VISIBLE);

        selectVisionModel = dao.getVision(selected_vision);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Toast.makeText(this, selectVisionModel.getTitle(), Toast.LENGTH_SHORT).show();

        tvToolbar.setText(selectVisionModel.getTitle());

        initTableViewAndTimeView();
    }

    @SuppressLint("InflateParams")
    private void initTableViewAndTimeView() {
        if (timeLayout.getChildAt(0) != null) {
            timeLayout.removeAllViews();
        }

        if (tableLayout.getChildAt(0) != null) {
            tableLayout.removeAllViews();
        }

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

        setTimeValues();

        setTableValues();
    }

    private void setTimeValues() {
        tvTime.setText(FaNum.convert(ChangeDate.getCurrentTime()));
        tvDate.setText(FaNum.convert(ChangeDate.getCurrentYear() + " / " + ChangeDate.getCurrentMonth() + " / " + ChangeDate.getCurrentDay()));
        long t = Long.parseLong(selectVisionModel.getMilli_sec());
        tvTimer.setText(timeFormat(t));
        tvDay.setText(showDay());

        tvTimer.setTextColor(selectVisionModel.getIs_tick().equals("0") ? getColor(R.color.primary_color) : getColor(R.color.gray));
        tvTitleTimer.setTextColor(selectVisionModel.getIs_tick().equals("0") ? getColor(R.color.primary_color) : getColor(R.color.gray));
    }

    private void setTableValues() {
        String isTick = selectVisionModel.getIs_tick();

        int income = Integer.parseInt(selectVisionModel.getIncome());
        titleIncome.setText(isTick.equals("0") ? getString(R.string.income_text) : getString(R.string.all_income_text));
        titleIncome.setTextColor(isTick.equals("0") ? (income == 0 ? getColor(R.color.primary_color) : getColor(R.color.accent_color)) : getColor(R.color.gray));

        int incomeAmount = Integer.parseInt(selectVisionModel.getIncome_amount());
        tvIncome.setText(isTick.equals("0") ? FaNum.convert(splitDigits(income)) + "   تومان" : FaNum.convert(splitDigits(incomeAmount)) + "   تومان");
        tvIncome.setTextColor(isTick.equals("0") ? (income == 0 ? getColor(R.color.primary_color) : getColor(R.color.accent_color)) : getColor(R.color.gray));

        //------------------------------------------------------------------------------------------

        int payment = Integer.parseInt(selectVisionModel.getPayment());
        titlePayment.setText(isTick.equals("0") ? getString(R.string.payment_text) : getString(R.string.all_rest_text));
        titlePayment.setTextColor(isTick.equals("0") ? (payment > 0 ? getColor(R.color.primary_color) : getColor(R.color.accent_color)) : getColor(R.color.gray));

        int amount = Integer.parseInt(selectVisionModel.getAmount());
        int restAmount = amount - incomeAmount;
        tvPayment.setText(isTick.equals("0") ? FaNum.convert(splitDigits(payment)) + "   تومان" : FaNum.convert(splitDigits(restAmount)) + "   تومان");
        tvPayment.setTextColor(isTick.equals("0") ? (payment > 0 ? getColor(R.color.primary_color) : getColor(R.color.accent_color)) : getColor(R.color.gray));

        //------------------------------------------------------------------------------------------

        int profit = Integer.parseInt(selectVisionModel.getProfit());
        titleProfit.setText(isTick.equals("0") ? (profit > 0 ? getString(R.string.profit_text) : getString(R.string.damage_text)) : getString(R.string.all_days_text));
        titleProfit.setTextColor(isTick.equals("0") ? (profit <= 0 ? getColor(R.color.primary_color) : getColor(R.color.accent_color)) : getColor(R.color.gray));

        String dayVision = selectVisionModel.getDay_vision();
        tvProfit.setText(isTick.equals("0") ? FaNum.convert(splitDigits(profit)) + "   تومان" : FaNum.convert(dayVision));
        tvProfit.setTextColor(isTick.equals("0") ? (profit <= 0 ? getColor(R.color.primary_color) : getColor(R.color.accent_color)) : getColor(R.color.gray));

        //------------------------------------------------------------------------------------------

        int rest = Integer.parseInt(selectVisionModel.getRest());
        titleRest.setText(isTick.equals("0") ? (rest > 0 ? getString(R.string.rest_text) : getString(R.string.extra_text)) : getString(R.string.rest_days_text));
        titleRest.setTextColor(isTick.equals("0") ? (rest > 0 ? getColor(R.color.primary_color) : getColor(R.color.accent_color)) : getColor(R.color.gray));

        String dayRest = selectVisionModel.getDay_rest();
        tvRest.setText(isTick.equals("0") ? FaNum.convert(splitDigits(rest)) + "   تومان" : FaNum.convert(dayRest));
        tvRest.setTextColor(isTick.equals("0") ? (rest > 0 ? getColor(R.color.primary_color) : getColor(R.color.accent_color)) : getColor(R.color.gray));
    }


    private String timeFormat(long t) {
        int sec = (int) (t / 1000);
        int min = sec / 60;
        int hour = min / 60;
        sec %= 60;
        min %= 60;
        return FaNum.convert(String.format(Locale.ENGLISH, "%02d", sec) + " : " + String.format(Locale.ENGLISH, "%02d", min) + " : " + String.format(Locale.ENGLISH, "%02d", hour));
    }

    private String showDay() {
        String result = "";
        int day = ShamsiCalendar.dayOfWeek(ShamsiCalendar.shSysDate());
        switch (day) {
            case 2:
                result = "دوشنبه";
                break;

            case 3:
                result = "سه شنبه";
                break;

            case 4:
                result = "چهارشنبه";
                break;

            case 5:
                result = "پنجشنبه";
                break;

            case 6:
                result = "جمعه";
                break;

            case 7:
                result = "شنبه";
                break;

            case 1:
                result = "یکشنبه";
        }
        return result;
    }

    public static String splitDigits(int number) {
        return new DecimalFormat("###,###,###,###,###,###").format(number);
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
            shared.getEditor().putBoolean(UserItems.IS_FIRST_TIME, false);
            shared.getEditor().putBoolean(UserItems.IS_USER_ADD_VISION, true);
            shared.getEditor().apply();
        }
    }

    @Override
    public void onSelectVisionDialogSelectListener(String titleSelectVision) {
        if (!isUserSelectVision) {
            title = titleSelectVision;
            shared.getEditor().putBoolean(UserItems.IS_USER_SELECT_VISION, true);
            shared.getEditor().putBoolean(UserItems.IS_USER_ADD_VISION, true);
            shared.getEditor().putString(UserItems.SELECTED_VISION, title);
            shared.getEditor().apply();
            startNormallyActivity();
        } else {
            startNormallyActivity();
        }
        selectVisionDialog.dismiss();
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
        tvToolbar = toolbar.findViewById(R.id.tv_toolbar);
        imgToolbar = toolbar.findViewById(R.id.img_toolbar);
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
        selected_vision = shared.getShared().getString(UserItems.SELECTED_VISION, "");
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