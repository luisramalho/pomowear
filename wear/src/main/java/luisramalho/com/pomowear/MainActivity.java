package luisramalho.com.pomowear;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    public static final String TAG = MainActivity.class.getSimpleName();

    public static final long WORKING_TIME_MILLIS = 25*60*1000 + 1000; // extra second for UX
    public static final long RESTING_TIME_MILLIS = 5*60*1000 + 1000; // extra second for UX

    private Button mStartButton;
    private TextView mTimer;
    private TextView mTimerStatus;
    private Boolean mIsWorkingStatus;
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
                        mIsWorkingStatus = true;
                        workTimer().start();
                    }
                });
            }
        });
    }

    private CountDownTimer workTimer() {
        mTimer.setTextColor(getResources().getColor(android.R.color.white));
        mTimerStatus.setText(getResources().getString(R.string.status_working));
        return startTimer(WORKING_TIME_MILLIS);
    }

    private CountDownTimer restTimer() {
        mTimer.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        mTimerStatus.setText(getResources().getString(R.string.status_resting));
        return startTimer(RESTING_TIME_MILLIS);
    }

    private CountDownTimer startTimer(long millis) {
        return new CountDownTimer(millis, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                long secondsUntilFinished = millisUntilFinished/1000;
                long minutes = (secondsUntilFinished % 3600) / 60;
                long seconds = secondsUntilFinished % 60;
                String timeLeft = String.format("%02d:%02d", minutes, seconds);
                mTimer.setText(timeLeft);
            }

            @Override
            public void onFinish() {
                mVibrator.vibrate(500);

                if (mIsWorkingStatus) {
                    restTimer().start();
                } else {
                    workTimer().start();
                }

                mIsWorkingStatus = !mIsWorkingStatus;
            }
        };
    }

}
