package luisramalho.com.pomowear;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    public static final String TAG = MainActivity.class.getSimpleName();

    PomodoroTimer mPomodoroTimer;
    private Button mStartButton;
    private TextView mTimer;
    private TextView mTimerStatus;
    private Vibrator mVibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mStartButton = (Button) stub.findViewById(R.id.start_button);
                mTimer = (TextView) stub.findViewById(R.id.timer);
                mTimerStatus = (TextView) stub.findViewById(R.id.timer_status);

                mStartButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mStartButton.setVisibility(View.GONE);
                        mTimer.setVisibility(View.VISIBLE);
                        mTimerStatus.setVisibility(View.VISIBLE);
                        mTimer.setTextColor(getResources().getColor(android.R.color.darker_gray));
                        mPomodoroTimer = new PomodoroTimer(new PomodoroCallback() {
                            @Override
                            public void onTick(String timeLeft) {
                                mTimer.setText(timeLeft);
                            }

                            @Override
                            public void workingTimeStarted() {
                                mVibrator.vibrate(500);
                                mTimer.setTextColor(getResources().getColor(android.R.color.white));
                                mTimerStatus.setText(getResources().getString(R.string.status_working));
                            }

                            @Override
                            public void restingTimeStarted() {
                                mVibrator.vibrate(500);
                                mTimer.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                                mTimerStatus.setText(getResources().getString(R.string.status_resting));
                            }
                        });
                    }
                });
            }
        });
    }
}
