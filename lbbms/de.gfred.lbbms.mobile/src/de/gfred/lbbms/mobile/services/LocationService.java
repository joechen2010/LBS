package de.gfred.lbbms.mobile.services;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import de.gfred.lbbms.mobile.services.ILocationService.Stub;

public class LocationService extends Service {
    private static final String TAG = "de.gfred.lbbms.mobile.service.LocationService";
    private static final boolean DEBUG = true;

    private static final long INTERVALL = 5000; // in miliseconds

    private Timer locationTimer;
    private NotificationManager notifcationManager;
    private LocationData currentLocation = new LocationData(123.0, 123.0, System.currentTimeMillis());

    private IUpdateLocationService updateService;
    private Intent updateServiceIntent;

    private ServiceConnection updateServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            updateService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            updateService = IUpdateLocationService.Stub.asInterface(service);
        }
    };

    @Override
    public void onCreate() {
        if (DEBUG) {
            Log.d(TAG, "services started...");
        }
        super.onCreate();

        updateServiceIntent = new Intent(IUpdateLocationService.class.getName());

        locationTimer = new Timer();

        checkLoaction();
        // notifcationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // showNotification();
        getApplicationContext().startService(updateServiceIntent);

    }

    @Override
    public void onDestroy() {
        if (DEBUG) {
            Log.d(TAG, "service stopped...");
        }
        super.onDestroy();
        if (locationTimer != null) {
            locationTimer.cancel();
        }
        getApplicationContext().stopService(updateServiceIntent);

    }

    private void checkLoaction() {
        locationTimer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                if (DEBUG) {
                    Log.d(TAG, "TimerTask");
                }
                bindService(updateServiceIntent, updateServiceConnection, Context.BIND_AUTO_CREATE);

                if (updateService != null) {
                    double dummy = new Random(System.currentTimeMillis()).nextDouble();
                    updateLocation(new LocationData(dummy, dummy / 2, System.currentTimeMillis()));
                }

                unbindService(updateServiceConnection);
                // SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LocationService.this);
                // Log.d(TAG, preferences.getString(Values.CUSTOMER_EMAIL, "NIX :("));
                // currentLocation =
            }
        }, 0, INTERVALL);
    }

    private void updateLocation(LocationData location) {

        try {
            updateService.updateLocation(location);
        } catch (RemoteException e) {
            Log.e(TAG, e.getMessage(), e);
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return locationBinder;
    }

    private final ILocationService.Stub locationBinder = new Stub() {

        @Override
        public LocationData getLocation() throws RemoteException {
            return currentLocation;
        }
    };

    // private void showNotification() {
    // // In this sample, we'll use the same text for the ticker and the expanded notification
    // CharSequence text = getText(R.string.app_name);
    //
    // // Set the icon, scrolling text and timestamp
    // Notification notification = new Notification(R.drawable.icon, text, System.currentTimeMillis());
    //
    // // The PendingIntent to launch our activity if the user selects this notification
    // PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this,
    // LocalServiceActivities.Controller.class), 0);
    //
    // // Set the info for the views that show in the notification panel.
    // notification.setLatestEventInfo(this, getText(R.string.hello), text, contentIntent);
    //
    // // Send the notification.
    // // We use a layout id because it is a unique number. We use it later to cancel.
    // notifcationManager.notify(R.string.hello, notification);
    // }
    //
    // public class LocationBinder extends Binder {
    // LocationService getService() {
    // return LocationService.this;
    // }
    // }
}
