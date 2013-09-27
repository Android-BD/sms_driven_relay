package com.yoctopuce.examples.ysmsrelay;

import android.app.Fragment;

/**
 * Created by seb on 19.09.13.
 */
public class SwitchListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new SwitchListFragment();
    }
}
