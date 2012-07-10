package de.gfred.lbbms.mobile.services;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import de.gfred.lbbms.mobile.util.Converter;
import de.gfred.lbbms.mobile.util.Values;

public class UpdateLocationService extends Service {
    private static final String TAG = "de.gfred.lbbms.mobile.service.UpdateLocationService";
    private static final boolean DEBUG = true;

    @Override
    public void onCreate() {
        if (DEBUG) {
            Log.d(TAG, "service started...");
        }
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        if (DEBUG) {
            Log.d(TAG, "service stopped...");
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return updateBinder;
    }

    private IUpdateLocationService.Stub updateBinder = new IUpdateLocationService.Stub() {

        @Override
        public void updateLocation(LocationData location) throws RemoteException {
            JSONObject object = Converter.convertLocationDataIntoJSONObject(location);
            try {
                HttpClient client = new DefaultHttpClient();
                client.getConnectionManager().getSchemeRegistry()
                        .register(new Scheme("https", new IgnoreSelfCertificatesSocketFactory(), Values.SCHEME_PORT));

                HttpPut put = new HttpPut(Values.CUSTOMER_URI);
                put.setEntity(new StringEntity(object.toString()));
                put.setHeader(Values.HEADER_ACCEPT, Values.MIME_TYPE_JSON);
                put.addHeader(Values.HEADER_TYPE, Values.MIME_TYPE_JSON);

                HttpResponse response = client.execute(put);

                Log.d(TAG, "Statuscode: " + response.getStatusLine().getStatusCode());

            } catch (ClientProtocolException e) {
                Log.e(TAG, e.getMessage(), e);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    };

}
