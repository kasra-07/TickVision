package ir.mahchegroup.tickvision;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

import ir.mahchegroup.tickvision.classes.Animations;
import ir.mahchegroup.tickvision.classes.FaNum;
import ir.mahchegroup.tickvision.classes.KeyboardManager;
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
import ir.mahchegroup.tickvision.message_box.UpdatePriceDialog;
import ir.mahchegroup.tickvision.network.AddVision;
import ir.mahchegroup.tickvision.network.ClearAllVisions;
import ir.mahchegroup.tickvision.network.GetAllVisions;
import ir.mahchegroup.tickvision.network.GetCountVision;
import ir.mahchegroup.tickvision.network.NetworkReceiver;
import ir.mahchegroup.tickvision.network.UpdatePrice;

public class HomeActivity extends AppCompatActivity implements GetCountVision.OnGetCountCallBack, UpdateAllVisionsDialog.OnUpdateAllVisionsDialogCallBack, ClearAllVisionsDialog.OnClearAllVisionsDialogCallBack, AddVisionDialog.OnAddVisionDialogCallBack, AddVision.OnAddVisionCallBack, ClearAllVisions.OnClearAllVisionsCallBack, GetAllVisions.OnGetAllVisionsCallBack, SelectVisionDialog.OnSelectVisionDialogCallBack, UpdatePriceDialog.OnUpdatePriceDialogCallBack, UpdatePrice.OnUpdatePriceCallBack {
    private NetworkReceiver receiver;
    private RelativeLayout btnAddVisionRoot, btnSelectVisionRoot;
    private LinearLayout btnAddLayout, btnSelectLayout;
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
    private int ofLineCount, price, oldIncome, newIncome, oldPayment, newPayment, oldProfit, newProfit, oldRest, newRest, oldIncomeAmount, newIncomeAmount, oldRestAmount, newRestAmount, oldAmount;
    private boolean isFirstTime, isUserAddVision, isUserSelectVision, isOpenMenu, isEquals = false, isBackEditActivity, isIncome;
    public static boolean isCheckDayMode;
    private LayoutInflater inflater;
    private String userTbl, date, title, amount, day, selectedVision, isTick;
    private ModelVision selectVisionModel;
    private UpdateAllVisionsDialog updateAllVisionsDialog;
    private ClearAllVisionsDialog clearAllVisionsDialog;
    private ClearAllVisions clearAllVisions;
    private AddVisionDialog addVisionDialog;
    private AddVision addVision;
    private GetAllVisions getAllVisions;
    private SelectVisionDialog selectVisionDialog;
    private UpdatePriceDialog updatePriceDialog;
    private UpdatePrice updatePrice;

    @SuppressLint("RtlHardcoded")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = android.R.id.home;
        if (item.getItemId() == id) {
            if (menu.isOpened()) {
                closeMenu();
                new Handler().postDelayed(() -> drawer.openDrawer(Gravity.RIGHT), 400);
            } else {
                if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                    drawer.closeDrawer(Gravity.RIGHT);
                } else {
                    drawer.openDrawer(Gravity.RIGHT);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setMenu() {
        menu.setOnMenuButtonClickListener(view -> {
            if (isOpenMenu) {
                closeMenu();
            } else {
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
                dimMenu.startAnimation(animation);
                dimMenu.setVisibility(View.VISIBLE);
                menu.open(true);
                isOpenMenu = true;
                dimMenu.setOnClickListener(view1 -> closeMenu());
            }
        });

        menu.getChildAt(0).setOnClickListener(view -> {
            closeMenu();
            new Handler().postDelayed(() -> addVisionDialog.show(), 400);
        });

        menu.getChildAt(1).setOnClickListener(view -> {
            closeMenu();
            isCheckDayMode = true;
            new Handler().postDelayed(() -> selectVisionDialog.show(), 400);
        });

        menu.getChildAt(2).setOnClickListener(view -> {
            closeMenu();
            isCheckDayMode = false;
            new Handler().postDelayed(() -> selectVisionDialog.show(), 400);
        });
    }

    private void closeMenu() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        dimMenu.startAnimation(animation);
        dimMenu.setVisibility(View.GONE);
        menu.close(true);
        isOpenMenu = false;
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

    @SuppressLint("InflateParams")
    private void setDisableViewsInPrice(String isTick) {
        if (isTick.equals("1")) {
            incomeLayout.removeAllViews();
            incomeView = inflater.inflate(R.layout.income_view_gray, null);
            incomeLayout.addView(incomeView);
            incomeLayout.setEnabled(false);

            paymentLayout.removeAllViews();
            paymentView = inflater.inflate(R.layout.payment_view_gray, null);
            paymentLayout.addView(paymentView);
            paymentLayout.setEnabled(false);

            timerSwitch.setImageResource(R.drawable.timer_disable);

            imgToolbar.setVisibility(View.VISIBLE);

        } else {
            incomeLayout.removeAllViews();
            incomeView = inflater.inflate(R.layout.income_view, null);
            incomeLayout.addView(incomeView);
            incomeLayout.setEnabled(true);

            paymentLayout.removeAllViews();
            paymentView = inflater.inflate(R.layout.payment_view, null);
            paymentLayout.addView(paymentView);
            paymentLayout.setEnabled(true);

            timerSwitch.setImageResource(R.drawable.timer_on);

            imgToolbar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            receiver = new NetworkReceiver();
            registerReceiver(receiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    //==============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

        if (isFirstTime) {
            GetCountVision getCountVision = new GetCountVision(this);
            getCountVision.getCount(userTbl);
        } else {
            if (!isUserAddVision) {
                setOnBtnAddClickListener();

            } else if (!isUserSelectVision) {
                setOnBtnAddClickListener();
                setOnBtnSelectClickListener();
                new Handler().postDelayed(() -> selectVisionDialog.show(), 500);

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

        if (isBackEditActivity && isEquals) {
            drawer.setVisibility(View.GONE);
            if (dao.getCountVision() > 0) {
                selectVisionDialog.show();
            } else {


                setOnBtnAddClickListener();
            }

        } else if (isBackEditActivity) {
            if (dao.getCountVision() == 0) {

                drawer.setVisibility(View.GONE);
                setOnBtnAddClickListener();

            } else {
                starting();
            }

        } else {
            starting();
        }

        isBackEditActivity = false;
        isEquals = false;
        shared.getEditor().putBoolean(UserItems.IS_BACK_EDIT_ACTIVITY, false);
        shared.getEditor().putBoolean(UserItems.IS_EQUALS_SELECTED_VISION, false);
        shared.getEditor().apply();
    }

    private void starting() {
        btnAddVisionRoot.setVisibility(View.GONE);
        btnSelectVisionRoot.setVisibility(View.GONE);
        drawer.setVisibility(View.VISIBLE);

        selectVisionModel = dao.getVision(selectedVision);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        tvToolbar.setText(selectVisionModel.getTitle());

        initTableViewAndTimeView();

        initIncomeViewAndPaymentView();

        setMenu();

        incomeLayout.setOnClickListener(v -> {
            isIncome = true;
            updatePriceDialog.show(true);
        });

        paymentLayout.setOnClickListener(v -> {
            isIncome = false;
            updatePriceDialog.show(false);
        });
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

        setTimeValues(selectVisionModel.getIs_tick());

        setTableValues(selectVisionModel.getIs_tick());
    }

    private void setTimeValues(String isTick) {
        tvTime.setText(FaNum.convert(ChangeDate.getCurrentTime()));
        tvDate.setText(FaNum.convert(ChangeDate.getCurrentYear() + " / " + ChangeDate.getCurrentMonth() + " / " + ChangeDate.getCurrentDay()));
        long t = Long.parseLong(selectVisionModel.getMilli_sec());
        tvTimer.setText(timeFormat(t));
        tvDay.setText(showDay());

        tvTimer.setTextColor(isTick.equals("0") ? getColor(R.color.primary_color) : getColor(R.color.gray));
        tvTitleTimer.setTextColor(isTick.equals("0") ? getColor(R.color.primary_color) : getColor(R.color.gray));
    }

    private void setTableValues(String isTick) {

        setDisableViewsInPrice(isTick);

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
        titleProfit.setText(isTick.equals("0") ? (profit >= 0 ? getString(R.string.profit_text) : getString(R.string.damage_text)) : getString(R.string.all_days_text));
        titleProfit.setTextColor(isTick.equals("0") ? (profit <= 0 ? getColor(R.color.primary_color) : getColor(R.color.accent_color)) : getColor(R.color.gray));

        String dayVision = selectVisionModel.getDay_vision();
        tvProfit.setText(isTick.equals("0") ? FaNum.convert(splitDigits(Math.abs(profit))) + "   تومان" : FaNum.convert(dayVision));
        tvProfit.setTextColor(isTick.equals("0") ? (profit <= 0 ? getColor(R.color.primary_color) : getColor(R.color.accent_color)) : getColor(R.color.gray));

        //------------------------------------------------------------------------------------------

        int rest = Integer.parseInt(selectVisionModel.getRest());
        titleRest.setText(isTick.equals("0") ? (rest >= 0 ? getString(R.string.rest_text) : getString(R.string.extra_text)) : getString(R.string.rest_days_text));
        titleRest.setTextColor(isTick.equals("0") ? (rest > 0 ? getColor(R.color.primary_color) : getColor(R.color.accent_color)) : getColor(R.color.gray));

        String dayRest = selectVisionModel.getDay_rest();
        tvRest.setText(isTick.equals("0") ? FaNum.convert(splitDigits(Math.abs(rest))) + "   تومان" : FaNum.convert(dayRest));
        tvRest.setTextColor(isTick.equals("0") ? (rest > 0 ? getColor(R.color.primary_color) : getColor(R.color.accent_color)) : getColor(R.color.gray));
    }

    @SuppressLint("InflateParams")
    private void initIncomeViewAndPaymentView() {
        if (incomeLayout.getChildAt(0) != null) {
            incomeLayout.removeAllViews();
        }

        if (paymentLayout.getChildAt(0) != null) {
            incomeLayout.removeAllViews();
        }

        String isTick = selectVisionModel.getIs_tick();

        incomeView = inflater.inflate(isTick.equals("0") ? R.layout.income_view : R.layout.income_view_gray, null);
        paymentView = inflater.inflate(isTick.equals("0") ? R.layout.payment_view : R.layout.payment_view_gray, null);

        incomeLayout.addView(incomeView);
        paymentLayout.addView(paymentView);

        incomeLayout.setEnabled(isTick.equals("0"));
        paymentLayout.setEnabled(isTick.equals("0"));
    }

    @Override
    public void onGetCountListener(int count) {
        if (count > 0) {
            ofLineCount = dao.getCountVision();

            if (count > ofLineCount) {
                updateAllVisionsDialog.show();
            } else {
                selectVisionDialog.show();
            }
        } else {
            setOnBtnAddClickListener();
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
            KeyboardManager.hideKeyboardOnActivity(this, this);

            new Handler().postDelayed(() -> addVisionDialog.dismiss(), 200);

            ToastMessage.show(this, getString(R.string.add_vision_success), true, true);


            if (!isUserAddVision) {
                shared.getEditor().putBoolean(UserItems.IS_FIRST_TIME, false).apply();
                shared.getEditor().putBoolean(UserItems.IS_USER_ADD_VISION, true).apply();
                selectVisionDialog.show();

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
        selectVisionDialog.show();
    }

    @Override
    public void onSelectVisionDialogSelectListener(String titleSelectVision) {
        title = titleSelectVision;
        selectVisionModel = dao.getVision(titleSelectVision);

        if (!isUserSelectVision) {
            shared.getEditor().putBoolean(UserItems.IS_USER_SELECT_VISION, true);
            shared.getEditor().putBoolean(UserItems.IS_USER_ADD_VISION, true);
            shared.getEditor().putString(UserItems.SELECTED_VISION, titleSelectVision);
            shared.getEditor().apply();
            Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
            finish();
        } else {
            if (isCheckDayMode) {
                shared.getEditor().putString(UserItems.SELECTED_VISION, titleSelectVision);
                shared.getEditor().apply();
                selectedVision = titleSelectVision;
                selectVisionModel = dao.getVision(titleSelectVision);
                startNormallyActivity();
                selectVisionDialog.dismiss();
            } else {
                shared.getEditor().putBoolean(UserItems.IS_BACK_EDIT_ACTIVITY, true);
                shared.getEditor().putString(UserItems.TITLE_SELECTED_VISION, titleSelectVision);
                Intent intent = new Intent(HomeActivity.this, EditActivity.class);
                intent.putExtra(UserItems.IS_EQUALS_SELECTED_VISION, titleSelectVision.equals(selectedVision));
                shared.getEditor().putBoolean(UserItems.IS_EQUALS_SELECTED_VISION, titleSelectVision.equals(selectedVision));
                shared.getEditor().apply();
                selectVisionDialog.dismiss();
                new Handler().postDelayed(() -> Animations.AnimActivity(this, intent), 300);
            }
        }
    }

    @Override
    public void onSelectVisionDialogCancelListener() {
        selectVisionDialog.dismiss();
        if (!isUserSelectVision) {
            setOnBtnAddClickListener();
            setOnBtnSelectClickListener();
        } else {
            startNormallyActivity();
        }
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(Gravity.RIGHT)) {
            drawer.closeDrawer(Gravity.RIGHT);
        } else if (menu.isOpened()) {
            closeMenu();
        } else {
            finish();
        }
    }

    @Override
    public void onUpdatePriceDialogListener(int price) {
        this.price = price;

        oldIncomeAmount = Integer.parseInt(selectVisionModel.getIncome_amount());
        oldRestAmount = Integer.parseInt(selectVisionModel.getRest_amount());
        oldIncome = Integer.parseInt(selectVisionModel.getIncome());
        oldPayment = Integer.parseInt(selectVisionModel.getPayment());
        oldProfit = Integer.parseInt(selectVisionModel.getProfit());
        oldRest = Integer.parseInt(selectVisionModel.getRest());
        oldAmount = Integer.parseInt(selectVisionModel.getAmount());

        if (isIncome) {
            newIncome = oldIncome + price;
            newPayment = oldPayment;
            newRest = oldRest - price;
            newIncomeAmount = oldIncomeAmount + price;
            newRestAmount = oldAmount - newIncomeAmount;
        } else {
            newIncome = oldIncome;
            newPayment = oldPayment + price;
            newRest = oldRest + price;
            newIncomeAmount = oldIncomeAmount - price;
            newRestAmount = oldAmount + newIncomeAmount;
        }
        newProfit = newIncome - newPayment;

        if (newRest <= 0) {
            isTick = "1";
            setDisableViewsInPrice(isTick);
        }
        updatePrice.set(userTbl, selectedVision, String.valueOf(newIncomeAmount), String.valueOf(newRestAmount), String.valueOf(newIncome), String.valueOf(newPayment), String.valueOf(newProfit), String.valueOf(newRest), isTick);
    }

    @Override
    public void onUpdatePriceListener(String isUpdate) {
        if (isUpdate.equals("success")) {
            selectVisionModel.setIncome_amount(String.valueOf(newIncomeAmount));
            selectVisionModel.setRest_amount(String.valueOf(newRestAmount));
            selectVisionModel.setIncome(String.valueOf(newIncome));
            selectVisionModel.setPayment(String.valueOf(newPayment));
            selectVisionModel.setProfit(String.valueOf(newProfit));
            selectVisionModel.setRest(String.valueOf(newRest));
            selectVisionModel.setIs_tick(isTick);

            dao.editVision(selectVisionModel);
            updatePriceDialog.dismiss();
            ToastMessage.show(this, getString(R.string.update_price_success_test), true, true);
            starting();
        } else {
            ToastMessage.show(this, getString(R.string.update_price_error), false, true);
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
        isCheckDayMode = shared.getShared().getBoolean(UserItems.IS_CHECK_DAY_MODE, true);
        userTbl = shared.getShared().getString(UserItems.USER_TBL, "");
        selectedVision = shared.getShared().getString(UserItems.SELECTED_VISION, "");
        dao = VisionDatabase.getVisionDatabase(this).visionDao();

        updateAllVisionsDialog = new UpdateAllVisionsDialog(this);
        clearAllVisionsDialog = new ClearAllVisionsDialog(this);
        clearAllVisions = new ClearAllVisions(this);
        addVisionDialog = new AddVisionDialog(this);
        addVision = new AddVision(this);
        getAllVisions = new GetAllVisions(this);
        selectVisionDialog = new SelectVisionDialog(this);
        updatePriceDialog = new UpdatePriceDialog(this);
        updatePrice = new UpdatePrice(this);
    }
}