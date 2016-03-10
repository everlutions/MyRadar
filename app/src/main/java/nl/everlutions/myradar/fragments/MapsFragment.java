package nl.everlutions.myradar.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import butterknife.ButterKnife;
import nl.everlutions.myradar.R;

public class MapsFragment extends BaseFragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    private LatLngBounds AUSTRALIA = new LatLngBounds(
            new LatLng(-44, 113), new LatLng(-10, 154));

    /**
     * CHECK OUT THE res/values/google_maps_api.xml ON HOW TO IMPLEMENT MAPS
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_with_map, container, false);
        ButterKnife.bind(this, view);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }


    // TO ENABLE MY LOCATION ADD THE NEXT TWO LINES TO YOUR MANIFEST
    //    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
    //    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.setMyLocationEnabled(true);

        // Set the camera to the greatest possible zoom level that includes the
        // bounds
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(AUSTRALIA, 0));
    }
}
