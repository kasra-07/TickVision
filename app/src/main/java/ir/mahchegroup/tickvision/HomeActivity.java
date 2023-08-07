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

public class HomeActivity extends AppCompatActivity {
    private RelativeLayout addVisionRoot, selectVisionRoot;
    private LinearLayout homeToolbarRoot, addBtnLayout, selectBtnLayout;
    private Toolbar toolbar;
    private TextView tvToolbar;
    private ImageView imgToolbar, addBtnDescription, selectBtnDescription, timerSwitch;
    private FloatingActionButton btnAdd, btnSelect;
    private DrawerLayout drawer;
    private View dimMenu;
    private CardView tableLayout, timeLayout, incomeLayout, paymentLayout;
    private FloatingActionMenu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
}