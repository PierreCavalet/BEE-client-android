package fr.pierrecavalet.bestexcuseever;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by pierre on 28/11/15.
 */
public class MyLocationListener implements LocationListener {

    public double longitude;
    public double latitude;

    @Override
    public void onLocationChanged(Location loc){
        longitude = loc.getLongitude();
        latitude = loc.getLatitude();
        System.out.println("latitude:" + latitude);
        System.out.println("longitude:" + longitude);
    }


    @Override
    public void onProviderDisabled(String provider){
    }

    @Override
    public void onProviderEnabled(String provider){
    }


    @Override

    public void onStatusChanged(String provider, int status, Bundle extras){
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

}