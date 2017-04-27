package net.edward.hello;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.*;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;



public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,LocationListener,GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnCameraIdleListener{

    private GoogleMap mMap;
    private TextView mMessage;
    Marker currentMarker;
    LatLng prevLatLng;
    LocationManager lms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mMessage = (TextView)findViewById(R.id.message);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        LocationManager status = (LocationManager) (this.getSystemService(Context.LOCATION_SERVICE));
        if (status.isProviderEnabled(LocationManager.GPS_PROVIDER) || status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            lms = (LocationManager) getSystemService(LOCATION_SERVICE); //change
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            Location location = lms.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            lms.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,0,this);
            if(mMap != null){
                setCamera(location);
                setMarker(location);
                setPolyLine(location);
            }



        } else {
            Toast.makeText(this, "請開啟定位服務", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnCameraIdleListener(this);

        LatLng FCU_IEE = new LatLng(24.179111, 120.649451);
        //mMap.addMarker(new MarkerOptions().position(FCU_IEE).title("逢甲大學資電館"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(FCU_IEE,15));
    }

    @Override
    public void onCameraIdle() {
        mMessage.setText("camera move ! ");
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMessage.setText("click !  point=" + latLng);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mMessage.setText("long click !  point=" + latLng);
    }

    @Override
    public void onLocationChanged(Location location) {
            if(mMap != null){
                setCamera(location);
                setMarker(location);
                setPolyLine(location);
            }

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


    private void setMarker(Location location) {
        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
        if(currentMarker == null){
            currentMarker = mMap.addMarker(new MarkerOptions().position(current).
                    title("Lat: " + location.getLatitude() +
                            " Long:" + location.getLongitude()));
        }else{
            currentMarker.setPosition(current);
            currentMarker.setTitle("Lat: " + location.getLatitude() +
                    " Long:" + location.getLongitude());
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
    }

    private void setCamera(Location location){
        mMap.moveCamera(CameraUpdateFactory.newLatLng(
                new LatLng(location.getLatitude(),location.getLongitude())));
    }

    private void setPolyLine(Location location){
        if(prevLatLng == null){
            prevLatLng = new LatLng(location.getLatitude(),location.getLongitude());
        }else{
            LatLng currentLatLng = new LatLng(location.getLatitude(),location.getLongitude()) ;
            mMap.addPolyline(new PolylineOptions()
                    .add(prevLatLng, currentLatLng).width(5).color(Color.BLUE));
        }
    }


}
