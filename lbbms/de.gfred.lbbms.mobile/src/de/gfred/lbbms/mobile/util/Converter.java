package de.gfred.lbbms.mobile.util;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import de.gfred.lbbms.mobile.services.LocationData;

public class Converter {
    private static final String TAG = "de.gfred.lbbms.mobile.util.Converter";

    public static JSONObject convertLocationDataIntoJSONObject(LocationData data) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(Values.JSON_LOCATION_LONGITUDE, data.longitude);
            obj.put(Values.JSON_LOCATION_LATITUDE, data.latitude);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return obj;
    }

    public static JSONObject createMSGJsonObject(String msg) {
        JSONObject obj = new JSONObject();

        try {
            obj.put(Values.MSG_SENDATE, System.currentTimeMillis());
            obj.put(Values.MSG_RECEIVER, "ALL");
            obj.put(Values.MSG_CONTENT, msg);
            obj.put(Values.MSG_LOCATION, convertLocationDataIntoJSONObject(new LocationData(2.0, 2.0, 123456789L)));
            obj.put(Values.MSG_TYPE, "BROADCAST");
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return obj;
    }
}
