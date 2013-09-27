package com.yoctopuce.examples.ysmsrelay;

import android.app.Fragment;

import java.util.UUID;

/**
 * Created by seb on 19.09.13.
 */
public class SwitchActivitiy extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        UUID yswitchId = (UUID)getIntent()
                .getSerializableExtra(SwitchFragment.EXTRA_SWITCH_ID);
        return SwitchFragment.newInstance(yswitchId);    }
}
