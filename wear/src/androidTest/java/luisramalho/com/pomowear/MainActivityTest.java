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

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mMainActivity;
    private Button mStartButton;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mMainActivity = getActivity();
        mStartButton = (Button) mMainActivity.findViewById(R.id.start_button);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testPreconditions() {
        assertNotNull("mMainActivity is null", mMainActivity);
        assertNotNull("mStartButton is null", mStartButton);
    }

    public void testStartButton_labelText() {
        final String expected = mMainActivity.getString(R.string.start_button);
        final String actual = mStartButton.getText().toString();
        assertEquals(expected, actual);
    }

    public void testButtonClickStartsPomodoro() {
        onView(withId(R.id.start_button))
            .perform(click());

        onView(withId(R.id.timer))
            .check(matches(isDisplayed()));
        onView(withId(R.id.timer_status))
            .check(matches(withText(mMainActivity.getString(R.string.status_working))));
    }

}
