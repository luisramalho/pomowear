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

import java.util.ArrayList;
import java.util.List;

public class CompositeListener implements PomodoroListener {

    private static CompositeListener _instance;

    private List<PomodoroListener> mRegisteredListeners = new ArrayList<>();

    public synchronized static CompositeListener getInstance() {
        if (_instance == null) {
            _instance = new CompositeListener();
        }
        return _instance;
    }

    public void registerListener(PomodoroListener listener) {
        mRegisteredListeners.add(listener);
    }

    @Override
    public void onTick(String timeLeft) {
        for(PomodoroListener listener : mRegisteredListeners) {
            listener.onTick(timeLeft);
        }
    }

    @Override
    public void workingTimeStarted() {
        for(PomodoroListener listener : mRegisteredListeners) {
            listener.workingTimeStarted();
        }
    }

    @Override
    public void restingTimeStarted() {
        for(PomodoroListener listener : mRegisteredListeners) {
            listener.restingTimeStarted();
        }
    }
}
