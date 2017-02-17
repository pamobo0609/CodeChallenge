package com.pamobo0609;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pamobo0609.constants.CodeChallengeConstants;
import com.pamobo0609.databinding.ActivityEarthquakeListBinding;
import com.pamobo0609.manager.RetrofitManager;
import com.pamobo0609.model.EarthquakeModel;
import com.pamobo0609.model.Feature;

import java.util.List;

/**
 * An activity representing a list of Earthquakes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link EarthquakeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class EarthquakeListActivity extends AppCompatActivity implements RetrofitManager.OnGetEarthquakesResponse {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

     ActivityEarthquakeListBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_earthquake_list);

        setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setTitle(getTitle());

        if (findViewById(R.id.earthquake_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        if (isConnected()) {
            RetrofitManager.getInstance().getEarthquakes(CodeChallengeConstants.QUERY_FORMAT, "2014-01-01", "2014-01-02",
                    this);
        } else {

        }

    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<Feature> pDataSet) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(pDataSet));
    }

    @Override
    public void onSuccess(EarthquakeModel pModel) {
        assert mBinding.listInclude.earthquakeList != null;
        setupRecyclerView(mBinding.listInclude.earthquakeList, pModel.getFeatures());
    }

    @Override
    public void onFailure() {
        Toast.makeText(this, R.string.err_connection, Toast.LENGTH_SHORT).show();
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Feature> mDataSet;

        public SimpleItemRecyclerViewAdapter(List<Feature> pDataSet) {
            mDataSet = pDataSet;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.earthquake_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mDataSet.get(position);

            holder.txtvLat.setText(String.valueOf(holder.mItem .getGeometry().getCoordinates().get(0)));
            holder.txtvLong.setText(String.valueOf(holder.mItem .getGeometry().getCoordinates().get(1)));
            holder.txtvMagnitude.setText(String.valueOf(holder.mItem .getProperties().getMag()));
            holder.txtvPlace.setText(holder.mItem .getProperties().getPlace());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        //arguments.putString(EarthquakeDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        EarthquakeDetailFragment fragment = new EarthquakeDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.earthquake_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, EarthquakeDetailActivity.class);
                        intent.putExtra(CodeChallengeConstants.LAT_KEY, holder.mItem.getGeometry().getCoordinates().get(0));
                        intent.putExtra(CodeChallengeConstants.LONG_KEY, holder.mItem.getGeometry().getCoordinates().get(1));

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDataSet.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView txtvPlace;
            public final TextView txtvMagnitude;
            public final TextView txtvLat;
            public final TextView txtvLong;

            public Feature mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                txtvPlace = (TextView) view.findViewById(R.id.txtv_place);
                txtvMagnitude = (TextView) view.findViewById(R.id.txtv_magnitude);
                txtvLat = (TextView) view.findViewById(R.id.txtv_lat);
                txtvLong = (TextView) view.findViewById(R.id.txtv_long);
            }

        }
    }
}
