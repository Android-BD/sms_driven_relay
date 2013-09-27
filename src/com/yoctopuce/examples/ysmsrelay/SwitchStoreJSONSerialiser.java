package com.yoctopuce.examples.ysmsrelay;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by seb on 23.09.13.
 */
public class SwitchStoreJSONSerialiser {

    private Context mContext;
    private String mFilename;

    public SwitchStoreJSONSerialiser(Context context, String filename) {
        mContext = context;
        mFilename = filename;
    }

    public ArrayList<YSwitch> loadYSwiches() throws IOException, JSONException {
        ArrayList<YSwitch> switches = new ArrayList<YSwitch>();
        BufferedReader reader = null;

        try {
            InputStream in = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line=reader.readLine())!=null) {
                jsonString.append(line);
            }
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            for (int i = 0; i < array.length(); i++) {
                switches.add(new YSwitch(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return switches;
    }


    public void savesYSwitches(ArrayList<YSwitch> switches) throws IOException, JSONException {
        JSONArray array = new JSONArray();
        for (YSwitch s : switches) {
            array.put(s.toJSON());
        }
        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }


    }

}
