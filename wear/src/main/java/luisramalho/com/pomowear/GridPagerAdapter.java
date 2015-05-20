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
import android.app.FragmentManager;
import android.content.Context;
import android.support.wearable.view.FragmentGridPagerAdapter;

public class GridPagerAdapter extends FragmentGridPagerAdapter {

    Context mContext;

    TimerFragment mTimerFragment;
    DetailsFragment mDetailsFragment;

    public GridPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;

        mTimerFragment = (TimerFragment) fm.findFragmentById(R.id.timer_fragment);
        if (mTimerFragment == null) {
            mTimerFragment = new TimerFragment();
        }
        mDetailsFragment = (DetailsFragment) fm.findFragmentById(R.id.details_fragment);
        if (mDetailsFragment == null) {
            mDetailsFragment = new DetailsFragment();
        }
    }

    @Override
    public Fragment getFragment(int row, int column) {
        if (column == 0) {
            return  mTimerFragment;
        } else {
            return mDetailsFragment;
        }
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount(int row) {
        return 2;
    }

}
