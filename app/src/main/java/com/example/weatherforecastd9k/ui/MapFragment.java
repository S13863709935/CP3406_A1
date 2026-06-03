package com.example.weatherforecastd9k.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.weatherforecastd9k.R;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.core.LatLonPoint;

public class MapFragment extends Fragment implements AMapLocationListener {
    private MapView mapView;
    private AMap aMap;
    private AMapLocationClient locationClient;
    private TextView cityNameText;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String PREFS_NAME = "WeatherSettings";
    private static final String DEFAULT_CITY_KEY = "default_city";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        // Set up privacy compliance before initializing the map
        try {
            // Update privacy compliance status
            com.amap.api.maps.MapsInitializer.updatePrivacyShow(requireContext(), true, true);
            com.amap.api.maps.MapsInitializer.updatePrivacyAgree(requireContext(), true);
            // Location privacy compliance
            AMapLocationClient.updatePrivacyShow(requireContext(), true, true);
            AMapLocationClient.updatePrivacyAgree(requireContext(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        
        // Initialize view
        mapView = view.findViewById(R.id.map);
        cityNameText = view.findViewById(R.id.cityName);
        mapView.onCreate(savedInstanceState);
        
        // Initialize map
        initMap();
        
        // Check permissions
        checkPermissions();
        
        return view;
    }

    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
            
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
            boolean autoLocation = prefs.getBoolean("auto_location", true);
            String defaultCity = prefs.getString("default_city", "");
            Log.d("cuckoo", "initMap: autoLocation=" + autoLocation + ", defaultCity=" + defaultCity);

            // Determine whether to enable the location blue dot based on settings
            MyLocationStyle myLocationStyle = new MyLocationStyle();
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
            myLocationStyle.interval(2000);
            aMap.setMyLocationStyle(myLocationStyle);
            aMap.setMyLocationEnabled(autoLocation);
            
            // Set up map UI controls
            aMap.getUiSettings().setMyLocationButtonEnabled(autoLocation);
            aMap.getUiSettings().setZoomControlsEnabled(true);
            
            // Determine whether to auto-locate based on settings
            if (!autoLocation && !defaultCity.isEmpty()) {
                moveToDefaultCity(defaultCity);
            }
        }
    }

    private void moveToDefaultCity(String city) {
        try {
            GeocodeSearch geocodeSearch = new GeocodeSearch(requireContext());
            geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                @Override
                public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {}

                @Override
                public void onGeocodeSearched(GeocodeResult result, int rCode) {
                    if (rCode == 1000) {
                        if (result != null && result.getGeocodeAddressList() != null 
                            && !result.getGeocodeAddressList().isEmpty()) {
                            GeocodeAddress address = result.getGeocodeAddressList().get(0);
                            LatLng latLng = new LatLng(address.getLatLonPoint().getLatitude(),
                                                     address.getLatLonPoint().getLongitude());
                            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                            cityNameText.setText(city);
                        }
                    }
                }
            });
            
            GeocodeQuery query = new GeocodeQuery(city, "");
            geocodeSearch.getFromLocationNameAsyn(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkPermissions() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        boolean autoLocation = prefs.getBoolean("auto_location", true);
        
        // Request permissions only when auto-location is enabled
        if (autoLocation) {
            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_CODE);
            } else {
                startLocation();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocation();
            } else {
                Toast.makeText(requireContext(), "Location permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startLocation() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        boolean autoLocation = prefs.getBoolean("auto_location", true);
        String defaultCity = prefs.getString("default_city", "");
        
        // Perform location only when auto-location is enabled
        if (autoLocation) {
            try {
                AMapLocationClient.updatePrivacyShow(requireContext(), true, true);
                AMapLocationClient.updatePrivacyAgree(requireContext(), true);
                locationClient = new AMapLocationClient(requireContext());
                locationClient.setLocationListener(this);
                
                AMapLocationClientOption option = new AMapLocationClientOption();
                option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                //option.setOnceLocation(true);
                locationClient.setLocationOption(option);
                
                locationClient.startLocation();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (!defaultCity.isEmpty()) {
            moveToDefaultCity(defaultCity);
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
                boolean autoLocation = prefs.getBoolean("auto_location", true);
                
                // Update location only when auto-location is enabled
                if (autoLocation) {
                    cityNameText.setText(aMapLocation.getCity());
                    LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                }
            } else {
                Log.d("MapFragment", "Location failed: " + aMapLocation.getErrorInfo());
                /*Toast.makeText(requireContext(),
                    "Location failed: " + aMapLocation.getErrorInfo(),
                    Toast.LENGTH_SHORT).show();*/
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (locationClient != null) {
            locationClient.onDestroy();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
} 