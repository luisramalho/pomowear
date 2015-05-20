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
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.wearable.view.WatchViewStub;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailsFragment extends Fragment implements PomodoroListener {
    public static final String TAG = DetailsFragment.class.getSimpleName();

    Resources mResources;
    TextView mPomodoriTextView;
    TextView mRestTextView;

    int mNumberOfPomodori;
    int mNumberOfBreaks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mResources = getResources();
        mNumberOfPomodori = 0;
        mNumberOfBreaks = 0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.details_fragment, container, false);

        final WatchViewStub stub = (WatchViewStub) root.findViewById(R.id.details_fragment);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mPomodoriTextView = (TextView) stub.findViewById(R.id.pomodoriTextView);
                mRestTextView = (TextView) stub.findViewById(R.id.restTextView);

                mPomodoriTextView.setText(prettyNumberOfPomodori(0));
                mRestTextView.setText(prettyNumberOfBreaks(0));

                // Register this fragment to the list of composite listeners
                CompositeListener.getInstance().registerListener(DetailsFragment.this);

                // Set the pomodoro listeners to the composite instance
                PomodoroTimer pomodoroTimer = PomodoroTimer.getInstance();
                pomodoroTimer.setPomodoroListener(CompositeListener.getInstance());
            }
        });

        return root;
    }

    /**
     * Converts the number of pomodori to a nice looking string.
     *
     * @param n number of pomodori.
     *
     * @return number of pomodori with plurals.
     */
    private String prettyNumberOfPomodori(int n) {
        return mResources.getQuantityString(R.plurals.numberOfPomodori, n, n);
    }

    /**
     * Converts the number of breaks to a nice looking string.
     *
     * @param n number of breaks.
     *
     * @return number of breaks with plurals.
     */
    private String prettyNumberOfBreaks(int n) {
        return mResources.getQuantityString(R.plurals.numberOfBreaks, n, n);
    }

    /*
        PomodoroListener overrides
     */

    @Override
    public void onTick(String timeLeft) {
        // Nothing to do here.
    }

    @Override
    public void workingTimeStarted() {
        mNumberOfPomodori++;
        mPomodoriTextView.setText(prettyNumberOfPomodori(mNumberOfPomodori));
    }

    @Override
    public void restingTimeStarted() {
        mNumberOfBreaks++;
        mRestTextView.setText(prettyNumberOfBreaks(mNumberOfBreaks));
    }
}
