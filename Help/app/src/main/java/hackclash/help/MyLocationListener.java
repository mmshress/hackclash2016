package hackclash.help;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Mohit on 1/2/2016.
 */
class MyLocationListener implements LocationListener {
    LatLng latLng;

    private static MyLocationListener instance= null;
    private Context context;

    public static MyLocationListener getInstance(Context context){
        if(instance == null){
            return new MyLocationListener(context);
        }
        return instance;
    }
    public MyLocationListener(Context context){
        this.context = context;

    }
    @Override
    public void onLocationChanged(Location location) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("spref",context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();

        double Latitude = location.getLatitude();
        Log.d("Latitude", String.valueOf(Latitude));
        double Longitude = location.getLongitude();
        Log.d("Longitude", String.valueOf(Longitude));

        sharedPrefEditor.putString("Latitude", Double.toString(Latitude));
        sharedPrefEditor.putString("Longitude", Double.toString(Longitude));

        sharedPrefEditor.commit();


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}