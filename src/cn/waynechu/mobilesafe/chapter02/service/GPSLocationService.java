package cn.waynechu.mobilesafe.chapter02.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;

public class GPSLocationService extends Service {
    private LocationManager lm;
    private MyListener listener;

    @Override
    public void onCreate() {
        super.onCreate();
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new MyListener();

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setCostAllowed(true);
        String name = lm.getBestProvider(criteria, true);
        System.out.println("最好的位置提供者：" + name);
        lm.requestLocationUpdates(name, 0, 0, listener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class MyListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            StringBuilder sb = new StringBuilder();
            sb.append("accuracy:" + location.getAccuracy() + "\n");
            sb.append("speed:" + location.getSpeed() + "\n");
            sb.append("jingdu:" + location.getLongitude() + "\n");
            sb.append("weidu:" + location.getLatitude() + "\n");
            String result = sb.toString();
            SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
            String safenumber = sp.getString("safephone", "");
            SmsManager.getDefault().sendTextMessage(safenumber, null, result,
                    null, null);
            stopSelf();
        }

        @Override
        public void onProviderDisabled(String arg0) {

        }

        @Override
        public void onProviderEnabled(String arg0) {

        }

        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lm.removeUpdates(listener);
        listener = null;
    }

}
