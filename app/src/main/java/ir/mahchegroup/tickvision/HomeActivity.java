package ir.mahchegroup.tickvision;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ir.mahchegroup.tickvision.classes.Shared;

public class HomeActivity extends AppCompatActivity {
    private RelativeLayout btnAddVisionRoot, btnSelectVisionRoot;
    private LinearLayout homeToolbarRoot, btnAddLayout, btnSelectLayout;
    private Toolbar toolbar;
    private TextView tvToolbar;
    private ImageView imgToolbar, btnAddDescription, btnSelectDescription, timerSwitch;
    private FloatingActionButton btnAdd, btnSelect;
    private DrawerLayout drawer;
    private View dimMenu;
    private CardView tableLayout, timeLayout, incomeLayout, paymentLayout;
    private View tableView, timeView, incomeView, paymentView;
    private FloatingActionMenu menu;
    private Shared shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
    }

    private void init() {
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
    }
}