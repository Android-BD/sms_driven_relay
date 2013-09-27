package com.yoctopuce.examples.ysmsrelay;

import android.content.Context;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by seb on 19.09.13.
 */
public class YSwitch {

    private final static String JSON_FIELD_NAME = "NAME";
    private final static String JSON_FIELD_UUID = "UUID";
    private final static String JSON_FIELD_STATE = "STATE";
    private final static String JSON_FIELD_INVERTED = "INVERTED";
    private final static String JSON_FIELD_PULSE = "PULSE";
    private final static String JSON_FIELD_HARDWAREID = "HARDWAREID";

    private String mName;
    private UUID mUUID;
    private boolean mOn;
    private boolean mInverted;
    private int mPulse;
    private String mHardwareId;
    private boolean mOnline;


    public YSwitch() {
        mUUID = UUID.randomUUID();
        mName ="";
        mOn = false;
        mInverted = false;
        mHardwareId = "";
        mPulse =0;
        mOnline=false;
    }

    public YSwitch(JSONObject jsonObject) throws JSONException {

        mUUID = UUID.fromString(jsonObject.getString(JSON_FIELD_UUID));
        mName = jsonObject.getString(JSON_FIELD_NAME);
        mOn = false;
        mInverted = jsonObject.getBoolean(JSON_FIELD_INVERTED);
        mHardwareId = jsonObject.getString(JSON_FIELD_HARDWAREID);
        mPulse = jsonObject.getInt(JSON_FIELD_PULSE);
        mOnline=false;
    }

    public String getName() {
        return mName;
    }

    public UUID getUUID() {
        return mUUID;
    }

    public boolean isInverted() {
        return mInverted;
    }

    public int getPulse() {
        return mPulse;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setUUID(UUID UUID) {
        mUUID = UUID;
    }


    public void setInverted(boolean inverted) {
        mInverted = inverted;
    }

    public void setPulse(int pulse) {
        mPulse = pulse;
    }


    public boolean isOn() {
        return mOn;
    }

    public void setOn(Context ctx, boolean newOn) {
        if(mOn == newOn)
            return;
        mOn = newOn;
        Intent msgIntent = new Intent(ctx, YoctoService.class);

        msgIntent.putExtra(YoctoService.PARAM_IN_YRELAY, mHardwareId);
        if(mPulse==0){
            msgIntent.putExtra(YoctoService.PARAM_IN_CMD, YoctoService.COMMANDS.SWITCH);
            if (mInverted)
                newOn = !newOn;
            msgIntent.putExtra(YoctoService.PARAM_IN_SWITCH_ON,newOn);
        } else {
            msgIntent.putExtra(YoctoService.PARAM_IN_CMD, YoctoService.COMMANDS.PULSE);
            msgIntent.putExtra(YoctoService.PARAM_IN_PULSE_LEN,mPulse);

        }

        ctx.startService(msgIntent);
    }



    public void refrsh(Context ctx) {
        Intent msgIntent = new Intent(ctx, YoctoService.class);
        msgIntent.putExtra(YoctoService.PARAM_IN_YRELAY, mHardwareId);
        msgIntent.putExtra(YoctoService.PARAM_IN_CMD, YoctoService.COMMANDS.REFRESH);
        ctx.startService(msgIntent);
    }





    @Override
    public String toString() {
        return mName;
    }

    public String getHardwareId() {
        return mHardwareId;
    }

    public void setHardwareId(String hardwareId) {
        mHardwareId = hardwareId;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_FIELD_NAME, mName);
        json.put(JSON_FIELD_UUID, mUUID.toString());
        json.put(JSON_FIELD_INVERTED, mInverted);
        json.put(JSON_FIELD_PULSE, mPulse);
        json.put(JSON_FIELD_HARDWAREID, mHardwareId);
        return json;
    }

    public boolean updateRelayState(boolean online, boolean on) {
        boolean changed =false;
        if(mOnline != online) {
            mOnline = online;
            changed = true;
        }
        if(mPulse==0){
            if(mInverted)
                on = !on;

            if(mOn != on){
                mOn =on;
                changed = true;
            }
        }
        return changed;
    }

    public boolean isOnline() {
        return mOnline;
    }
}
