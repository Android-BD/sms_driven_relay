package com.yoctopuce.examples.ysmsrelay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by seb on 20.09.13.
 */
public class YoctoUpdateReceiver extends BroadcastReceiver {
    public static final String ACTION_RESP = "com.yoctopuce.examples.YOCTO_UPDATE_RECEIVER";
    private final static String TAG = "YoctoUpdateReceiver";
    public final static String PARAM_RELAY_STATE = "PARAM_RELAY_STATE";
    public final static String PARAM_RELAY_ONLINE = "PARAM_RELAY_ONLINE";
    public final static String PARAM_RELAY_HARDWAREID = "PARAM_RELAY_HARDWAREID";
    public static final String PARAM_RELAY_LIST = "PARAM_RELAY_LIST";


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Received intent: " + intent);
        SwitchStore switchStore = SwitchStore.get(context);
        String[] relayList = intent.getStringArrayExtra(PARAM_RELAY_LIST);
        if(relayList!=null){
            newRelayList(context,relayList);
        }
        String hwid = intent.getStringExtra(PARAM_RELAY_HARDWAREID);
        if(hwid!=null){
            boolean state = intent.getBooleanExtra(PARAM_RELAY_STATE,false);
            boolean online = intent.getBooleanExtra(PARAM_RELAY_ONLINE,false);
            ArrayList<YSwitch> changed = switchStore.updateRelayState(hwid, online, state);
            for(YSwitch s : changed) {
                onSwitchChange(context,s);
            }
        }
    }

    protected void onSwitchChange(Context context,YSwitch yswitch)
    {
        Log.i(TAG, "Switch "+yswitch+" changed");
    }


    protected void newRelayList(Context context,String[] relayList)
    {
        Log.i(TAG, "Received " + Integer.toString(relayList.length)+" relay list");
    }
}
