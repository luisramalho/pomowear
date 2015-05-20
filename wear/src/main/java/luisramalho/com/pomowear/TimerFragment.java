/*
 * Copyright (C) 2015 Luís Ramalho
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package luisramalho.com.pomowear;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.wearable.view.WatchViewStub;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class TimerFragment extends Fragment implements PomodoroListener {
    public static final String TAG = TimerFragment.class.getSimpleName();

    private Button mStartButton;
    private TextView mTimer;
    private TextView mTimerStatus;
    private Vibrator mVibrator;
    private Resources mRes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mVibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        mRes = getResources();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.timer_fragment, container, false);

        final WatchViewStub stub = (WatchViewStub) root.findViewById(R.id.timer_fragment);
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
                        mTimer.setTextColor(mRes.getColor(android.R.color.darker_gray));

                        // Register this fragment to the list of composite listeners
                        CompositeListener.getInstance().registerListener(TimerFragment.this);

                        // Set the pomodoro listeners to the composite instance
                        PomodoroTimer pomodoroTimer = PomodoroTimer.getInstance();
                        pomodoroTimer.setPomodoroListener(CompositeListener.getInstance());

                        // Start the working pomodoro
                        pomodoroTimer.workingTimer().start();
                    }
                });
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        PomodoroTimer.getInstance().destroy();
        super.onDestroyView();
    }

    /*
        PomodoroListener overrides
     */

    @Override
    public void onTick(String timeLeft) {
        mTimer.setText(timeLeft);
    }

    @Override
    public void workingTimeStarted() {
        mVibrator.vibrate(500);
        mTimer.setTextColor(mRes.getColor(android.R.color.white));
        mTimerStatus.setText(mRes.getString(R.string.status_working));
    }

    @Override
    public void restingTimeStarted() {
        mVibrator.vibrate(500);
        mTimer.setTextColor(mRes.getColor(android.R.color.holo_green_dark));
        mTimerStatus.setText(mRes.getString(R.string.status_resting));
    }
}
