package com.yoctopuce.examples.ysmsrelay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by seb on 19.09.13.
 */
public class SwitchStore {
    public static final String FILENAME = "yswitch.json";
    private static final String TAG = "SwitchStore";
    private static SwitchStore sSwitchStore;
    private ArrayList<YSwitch> mYSwitches;
    private Context mAppContext;
    private SwitchStoreJSONSerialiser mJSONSerialiser;

    private BroadcastReceiver bcastReceiver = new YoctoUpdateReceiver();

    private SwitchStore(Context ctx) {
        mAppContext = ctx;
        mJSONSerialiser = new SwitchStoreJSONSerialiser(ctx, FILENAME);
        try {
            mYSwitches = mJSONSerialiser.loadYSwiches();
        } catch (Exception e) {
            e.printStackTrace();
            mYSwitches = new ArrayList<YSwitch>();
            Log.e(TAG, "Error loading switches: " + e);
        }
    }

    public static SwitchStore get(Context ctx){
        if (sSwitchStore== null){
            sSwitchStore = new SwitchStore(ctx.getApplicationContext());
        }
        return sSwitchStore;
    }

    public ArrayList<YSwitch> getYSwitches() {
        return mYSwitches;
    }

    public YSwitch getSwitch(UUID uuid) {
        for (YSwitch s  : mYSwitches){
            if (s.getUUID().equals(uuid))
                return s;
        }
        return null;
    }


    public void addSwitch(YSwitch ySwitch) {
        mYSwitches.add(ySwitch);
        saveSwitches();
    }

    public void deleteSwitch(YSwitch ySwitch) {
        mYSwitches.remove(ySwitch);
        saveSwitches();
    }

    public boolean saveSwitches() {
        try {
            mJSONSerialiser.savesYSwitches(mYSwitches);
        } catch (Exception e) {
            String msg = "Error saving switches: " + e;
            Log.e(TAG, msg);
            Toast.makeText(mAppContext, msg, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    public ArrayList<YSwitch> updateRelayState(String hwid, boolean isOnline, boolean isOn) {
        ArrayList<YSwitch> changed = new ArrayList<YSwitch>();
        for (YSwitch s : mYSwitches) {
            if (s.getHardwareId().equals(hwid)) {
                if (s.updateRelayState(isOnline, isOn))
                    changed.add(s);
            }
        }
        return changed;
    }

    public YSwitch getSwitch(String name) {
        for (YSwitch s  : mYSwitches){
            if (s.getName().equals(name))
                return s;
        }
        return null;
    }
}

