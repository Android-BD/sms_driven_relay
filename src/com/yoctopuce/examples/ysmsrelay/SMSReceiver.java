package com.yoctopuce.examples.ysmsrelay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by seb on 24.09.13.
 */
public class SMSReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            SmsMessage[] messages = new SmsMessage[pdus.length];
            for (int i = 0; i < pdus.length; i++)
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            for (SmsMessage message : messages) {
                String msg = message.getMessageBody();
                //long when = message.getTimestampMillis();
                String from = message.getOriginatingAddress();
                if(!parseIncomingSMS(context, from, msg)){
                    //this.abortBroadcast();
                }
            }
        }
    }

    private enum Cmd {
        TOGGLE,ON,OFF, UNKNOWN
    };



    protected  boolean parseIncomingSMS(Context ctx, String from,String msg) {
        Toast.makeText(ctx, from + ": " + msg, Toast.LENGTH_LONG).show();
        //todo: use filter to accept connection from only one number
        //TODO: use custom prefix and save that in a settings files

        String prefix = "yocto";
        if (prefix.length()>0) {
            if( !msg.startsWith(prefix))
                return false;
            msg = msg.substring(prefix.length());
        }

        String target = null;
        Cmd cmd = Cmd.UNKNOWN;

         if (msg.startsWith("toggle")) {
            target = msg.substring(6).trim();
            cmd = Cmd.TOGGLE;
         }else if (msg.startsWith("switch on")){
             target = msg.substring(9).trim();
             cmd = Cmd.ON;
         }else if (msg.startsWith("switch off")){
             target = msg.substring(10).trim();
             cmd = Cmd.OFF;
         }else if(msg.startsWith("switch ")){
             if(msg.endsWith(" on")){
                target = msg.substring(7,msg.length()-3);
                 cmd = Cmd.ON;
             }else if (msg.endsWith(" off")){
                 target = msg.substring(7,msg.length()-4);
                 cmd = Cmd.OFF;
             }else{
                 target = msg.substring(7);
                 cmd = Cmd.TOGGLE;
             }
        }
        if(cmd.equals(Cmd.UNKNOWN))
            return false;
        YSwitch s = SwitchStore.get(ctx).getSwitch(target);
        String appname = ctx.getString(R.string.app_name);
        if(s==null) {
            Toast.makeText(ctx, appname+" : no switch named " +target, Toast.LENGTH_LONG).show();
            return false;
        }
        switch (cmd) {
            case TOGGLE:
                Toast.makeText(ctx, appname+" : toggle " +target, Toast.LENGTH_LONG).show();
                s.setOn(ctx,!s.isOn());
                break;
            case ON:
                Toast.makeText(ctx, appname+" : switch on " +target, Toast.LENGTH_LONG).show();
                s.setOn(ctx,true);
                break;
            case OFF:
                Toast.makeText(ctx, appname+" : switch off " +target, Toast.LENGTH_LONG).show();
                s.setOn(ctx,false);
                break;
            default:
                return false;
        }

        return true;
    }

}
