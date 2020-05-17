package com.example.listofitems;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;




public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,View.OnClickListener,LocationListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,GoogleMap.OnMapLongClickListener{

    private GoogleMap mMap;
    Button done;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    LatLng latLng1;
    LatLng latLng2;
    PolylineOptions routeLine = new PolylineOptions();
    int x=0;
    int cost=0;
    String location1,location2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Button done1;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        done=findViewById(R.id.done);
        done.setOnClickListener(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
         SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


            mapFragment.getMapAsync(this);



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    public int CalculationByDistance() {
        int Radius = 6371;// radius of earth in Km
        double lat1 = latLng1.latitude;
        double lat2 = latLng2.latitude;
        double lon1 = latLng1.longitude;
        double lon2 = latLng2.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        //Toast.makeText(getApplicationContext(),"the cost is Rs."+ kmInDec, Toast.LENGTH_LONG).show();
        return kmInDec;
    }
    public void searchLocation1(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.editText1);
         location1 = locationSearch.getText().toString();
        List<Address>[] addressList = new List[]{null};
        if (location1 != null || !location1.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList[0] = geocoder.getFromLocationName(location1, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList[0].get(0);
            latLng1 = new LatLng(address.getLatitude(), address.getLongitude());
            routeLine.add(latLng1).color(Color.BLACK);
            mMap.addMarker(new MarkerOptions().position(latLng1).title(location1).draggable(true).visible(true));
            mMap.setOnMapLongClickListener(this);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 14));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng1));
            Toast.makeText(getApplicationContext(),address.getLatitude()+" "+address.getLongitude(),Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),"To remove previous location markers,long press on the map!",Toast.LENGTH_LONG).show();
        }
    }

    public void searchLocation2(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.editText2);
         location2 = locationSearch.getText().toString();
        List<Address> addressList = null;
        if (location2 != null || !location2.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location2, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            latLng2 = new LatLng(address.getLatitude(), address.getLongitude());
            routeLine.add(latLng2).color(Color.BLACK);
            mMap.addPolyline(routeLine);
            mMap.addMarker(new MarkerOptions().position(latLng2).title(location2).draggable(true).visible(true).icon(BitmapDescriptorFactory.defaultMarker((BitmapDescriptorFactory.HUE_GREEN))));
            mMap.setOnMapLongClickListener(this);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng2, 14));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng2));
            Toast.makeText(getApplicationContext(), address.getLatitude() + " " + address.getLongitude(), Toast.LENGTH_LONG).show();
            x = CalculationByDistance();
            Toast.makeText(getApplicationContext(), "The distance is : " + String.valueOf(x)+"Km", Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.clear();
    }



    @Override
    public void onClick(View v) {

Intent is=getIntent();
String id=is.getStringExtra("id");
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("details").child(id);


        x=CalculationByDistance();
         cost=x*10;
        dr.child("pickup").setValue(location1);
        dr.child("drop").setValue(location2);
        dr.child("amount").setValue(cost);
        Intent intent = new Intent(MapsActivity.this, Payment.class);
        intent.putExtra("del_cost",String.valueOf(cost));
        startActivity(intent);
    }
}