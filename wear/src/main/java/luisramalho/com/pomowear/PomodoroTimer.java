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

/**
 * The basics of the pomodoro technique are:
 *
 * 1. Work for 25 minutes
 * 2. Take a short break (5 minutes)
 * 3. Every 4 pomodori take a longer break (15 minutes)
 */
public class PomodoroTimer {
    /**
     * Pomodoro working time, 25 minutes.
     */
    public static final long WORKING_TIME_MILLIS = 25*60*1000;

    /**
     * Pomodoro short break, 5 minutes.
     */
    public static final long SHORT_BREAK_TIME_MILLIS = 5*60*1000;

    /**
     * Pomodoro long break, 15 minutes.
     */
    public static final long LONG_BREAK_TIME_MILLIS = 15*60*1000;

    /**
     * The Singleton instance of this class.
     */
    private static PomodoroTimer _instance;

    /**
     * Callback so that activity knows what's going on.
     */
    private PomodoroListener mPomodoroListener;

    /**
     * If the pomodoro is in working status or not.
     */
    private Boolean mIsWorkingStatus;

    /**
     * Number of pomodori
     */
    private int mNumberOfPomodori;

    /**
     * Constructor, instantiates the pomodoro callback, the working status boolean
     * and starts the working timer.
     */
    public PomodoroTimer() {
        mIsWorkingStatus = true;
        mNumberOfPomodori = 0;
    }

    /**
     * Singleton, the most we will need is one timer.
     *
     * @return the only instance of PomodoroTimer.
     */
    public synchronized static PomodoroTimer getInstance() {
        if (_instance == null) {
            _instance = new PomodoroTimer();
        }
        return _instance;
    }

    /**
     * Starts the timer for working.
     *
     * @return timer with 25 minutes.
     */
    public CountDownTimer workingTimer() {
        mNumberOfPomodori++;
        mPomodoroListener.workingTimeStarted();
        return startTimer(WORKING_TIME_MILLIS);
    }

    /**
     * Starts the timer for the short break.
     *
     * @return timer with 5 minutes.
     */
    public CountDownTimer shortBreakTimer() {
        mPomodoroListener.restingTimeStarted();
        return startTimer(SHORT_BREAK_TIME_MILLIS);
    }

    /**
     * Starts the timer for the longer break.
     *
     * @return timer with 15 minutes.
     */
    public CountDownTimer longBreakTimer() {
        mPomodoroListener.restingTimeStarted();
        return startTimer(LONG_BREAK_TIME_MILLIS);
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
                if (_instance == null) {
                    this.cancel(); // Stop timer if instance was destroyed
                }
                String timeLeft = millisToHourMinute(millisUntilFinished);
                mPomodoroListener.onTick(timeLeft);
            }

            @Override
            public void onFinish() {
                if (mIsWorkingStatus) {
                    if (mNumberOfPomodori == 4) {
                        longBreakTimer().start();
                        mNumberOfPomodori = 0; // reset
                    } else {
                        shortBreakTimer().start();
                    }
                } else {
                    workingTimer().start();
                }

                // Reverses the boolean value
                mIsWorkingStatus = !mIsWorkingStatus;
            }
        };
    }

    /**
     * Sets the pomodoro listener callback.
     *
     * @param callback the callback.
     */
    public void setPomodoroListener(PomodoroListener callback) {
        mPomodoroListener = callback;
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

    /**
     * Remove references to this instance, ie destroy it.
     */
    protected void destroy() {
        _instance = null;
    }
}
