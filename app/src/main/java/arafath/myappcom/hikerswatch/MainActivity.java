package arafath.myappcom.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    TextView latText,lonText,accText, altText,addText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
        updateLocation(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else{
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,0,0,locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastKnownLocation != null){

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
           startListening();
        }
    }

    public void startListening(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    public void updateLocation(Location location){
        latText = findViewById(R.id.latText);
        lonText = findViewById(R.id.lonText);
        accText = findViewById(R.id.accText);
        altText = findViewById(R.id.altText);
        addText = findViewById(R.id.addText);

        latText.setText("Latitude: "+Double.toString(location.getLatitude()));
        lonText.setText("Longitude: "+Double.toString(location.getLongitude()));
        accText.setText("Accuracy: "+Double.toString(location.getAccuracy()));
        altText.setText("Altitude: "+Double.toString(location.getAltitude()));

        String address = "Could Not find Address :(";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> listAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if(listAddress != null && listAddress.size() > 0){
                address = "Address:\n";

                if(listAddress.get(0).getThoroughfare() != null){
                    address += listAddress.get(0).getThoroughfare() + "\n";
                }

                if(listAddress.get(0).getLocality() != null){
                    address += listAddress.get(0).getLocality()+ " ";
                }

                if(listAddress.get(0).getPostalCode() != null){
                    address += listAddress.get(0).getPostalCode()+ " ";
                }

                if(listAddress.get(0).getAdminArea() != null){
                    address += listAddress.get(0).getAdminArea();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        addText.setText(address);

    }


}
