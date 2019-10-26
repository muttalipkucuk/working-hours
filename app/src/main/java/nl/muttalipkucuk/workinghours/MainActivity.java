package nl.muttalipkucuk.workinghours;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import nl.muttalipkucuk.workinghours.utils.TimeUtils;

public class MainActivity extends AppCompatActivity {
    private static final String TOAST_REFRESH_CLICKED = "Current time is refreshed.";
    private static final String TOAST_START_CLICKED = "Started ...";
    private static final String TOAST_STOP_CLICKED = "... stopped.";
    private static final String TOAST_SAVE_CLICKED = "Saved !";

    private BottomNavigationView navView;
    private TextView mTextMessage;
    private TextView tvMessageCurrent;
    private TextView tvCurrentTime;
    private TextView tvStartTime;
    private TextView tvStopTime;
    private TextView tvSave;
    private Button btRefresh;
    private Button btStart;
    private Button btStop;
    private Button btSave;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        tvMessageCurrent = findViewById(R.id.tv_message_current_time);
        tvCurrentTime = findViewById(R.id.tv_current_time);
        tvStartTime = findViewById(R.id.tv_start_time);
        tvStopTime = findViewById(R.id.tv_stop_time);
        tvSave = findViewById(R.id.tv_save);
        btRefresh = findViewById(R.id.bt_refresh);
        btStart = findViewById(R.id.bt_start);
        btStop = findViewById(R.id.bt_stop);
        btSave = findViewById(R.id.bt_save);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        updateCurrentTime();

        btRefresh.setOnClickListener(v -> {
            updateCurrentTime();
            Toast.makeText(getApplicationContext(), TOAST_REFRESH_CLICKED, Toast.LENGTH_SHORT).show();
        });
        btStart.setOnClickListener(v -> {
            tvStartTime.setText(tvCurrentTime.getText());
            tvStartTime.setVisibility(View.VISIBLE);
            tvStopTime.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), TOAST_START_CLICKED, Toast.LENGTH_SHORT).show();
        });
        btStop.setOnClickListener(v -> {
            String currentTime = TimeUtils.getCurrentTime.apply();
            tvStopTime.setText(currentTime);
            tvStopTime.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), TOAST_STOP_CLICKED, Toast.LENGTH_SHORT).show();
        });
        btSave.setOnClickListener(v -> {
            String summaryText = TimeUtils.getSummaryText.apply(tvStartTime.getText().toString(), tvStopTime.getText().toString());
            tvSave.setText(summaryText);
            tvSave.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), TOAST_SAVE_CLICKED, Toast.LENGTH_SHORT).show();
        });
    }

    private void updateCurrentTime() {
        tvCurrentTime.setText(TimeUtils.getCurrentTime.apply());
    }
}
