package com.pamobo0609;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pamobo0609.constants.CodeChallengeConstants;

/**
 * A fragment representing a single Earthquake detail screen.
 * This fragment is either contained in a {@link EarthquakeListActivity}
 * in two-pane mode (on tablets) or a {@link EarthquakeDetailActivity}
 * on handsets.
 */
public class EarthquakeDetailFragment extends Fragment implements OnMapReadyCallback {

    private double mLatitude;

    private double mLongitude;

    private MapView mapView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EarthquakeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!getArguments().isEmpty()) {
            mLatitude = getArguments().getDouble(CodeChallengeConstants.LAT_KEY);
            mLongitude = getArguments().getDouble(CodeChallengeConstants.LONG_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.earthquake_detail, container, false);

        mapView = (MapView) v.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng coordinates = new LatLng(mLatitude, mLongitude);
        googleMap.addMarker(new MarkerOptions().position(coordinates).title("Earthquake location"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
    }
}