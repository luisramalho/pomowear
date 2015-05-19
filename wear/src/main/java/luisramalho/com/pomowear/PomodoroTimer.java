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

import android.os.CountDownTimer;

public class PomodoroTimer {
    /**
     * Pomodoro working time, 25 minutes.
     */
    public static final long WORKING_TIME_MILLIS = 25*60*1000;

    /**
     * Pomodoro resting time, 5 minutes.
     */
    public static final long RESTING_TIME_MILLIS = 5*60*1000;

    /**
     * Callback so that activity knows what's going on.
     */
    PomodoroCallback mPomodoroCallback;

    /**
     * If the pomodoro is in working status or not.
     */
    private Boolean mIsWorkingStatus;

    /**
     * Constructor, instantiates the pomodoro callback, the working status boolean
     * and starts the working timer.
     *
     * @param pomodoroCallback Callback to keep activity informed.
     */
    public PomodoroTimer(PomodoroCallback pomodoroCallback) {
        mPomodoroCallback = pomodoroCallback;
        mIsWorkingStatus = true;
        workingTimer().start();
    }

    /**
     * Starts the timer for the working pomodoro.
     *
     * @return timer with 25 minutes.
     */
    public CountDownTimer workingTimer() {
        return startTimer(WORKING_TIME_MILLIS);
    }

    /**
     * Starts the timer for the resting pomodoro.
     *
     * @return timer with 5 minutes.
     */
    public CountDownTimer restingTimer() {
        return startTimer(RESTING_TIME_MILLIS);
    }

    /**
     * Main timer function, other timers call this one.
     *
     * @param millis the milliseconds for the timer.
     *
     * @return a CountDownTimer.
     */
    private CountDownTimer startTimer(long millis) {
        return new CountDownTimer(millis, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                String timeLeft = millisToHourMinute(millisUntilFinished);
                mPomodoroCallback.onTick(timeLeft);
            }

            @Override
            public void onFinish() {
                if (mIsWorkingStatus) {
                    restingTimer().start();
                    mPomodoroCallback.restingTimeStarted();
                } else {
                    workingTimer().start();
                    mPomodoroCallback.workingTimeStarted();
                }

                // Reverses the boolean value
                mIsWorkingStatus = !mIsWorkingStatus;
            }
        };
    }

    /**
     * Converts milliseconds to a readable time format.
     *
     * @param millis the milliseconds.
     *
     * @return String in format HH:MM.
     */
    private String millisToHourMinute(long millis) {
        long secondsUntilFinished = millis / 1000;
        long minutes = (secondsUntilFinished % 3600) / 60;
        long seconds = secondsUntilFinished % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
