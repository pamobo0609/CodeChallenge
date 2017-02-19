package com.pamobo0609;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pamobo0609.constants.CodeChallengeConstants;
import com.pamobo0609.databinding.ActivityEarthquakeDetailBinding;

/**
 * An activity representing a single Earthquake detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link EarthquakeListActivity}.
 */
public class EarthquakeDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;

    ActivityEarthquakeDetailBinding mBinding;

    private double mLatitude;

    private double mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_earthquake_detail);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            mLatitude = extras.getDouble(CodeChallengeConstants.LAT_KEY);
            mLongitude = extras.getDouble(CodeChallengeConstants.LONG_KEY);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // When the savedInstanceState is null, it means that a fragment is being used.
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putDouble(CodeChallengeConstants.LAT_KEY, mLatitude);
            arguments.putDouble(CodeChallengeConstants.LONG_KEY, mLongitude);

            EarthquakeDetailFragment fragment = new EarthquakeDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.earthquake_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, EarthquakeListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * <h1>OnMapReady</h1>
     * <p>Executed when the {@link GoogleMap} is ready.</p>
     *
     * @param googleMap the {@link GoogleMap} in the fragment.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng coordinates = new LatLng(mLatitude, mLongitude);
        mMap.addMarker(new MarkerOptions().position(coordinates).title("Earthquake location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
    }
}
