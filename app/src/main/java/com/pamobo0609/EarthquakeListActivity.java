package com.pamobo0609;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.pamobo0609.constants.CodeChallengeConstants;
import com.pamobo0609.databinding.ActivityEarthquakeListBinding;
import com.pamobo0609.datasource.EarthquakeDataSource;
import com.pamobo0609.manager.RetrofitManager;
import com.pamobo0609.model.EarthquakeModel;
import com.pamobo0609.model.Feature;
import com.pamobo0609.service.DatabaseService;

import java.util.Calendar;
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

    private ProgressDialog progressDialog;

    private String startDate;

    private String endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_earthquake_list);

        setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setTitle(getTitle());

        if (findViewById(R.id.earthquake_detail_container) != null) {
            // When this pane is present, it means that is a tablet.
            mTwoPane = true;
        }

        // Render the rows from the service or from the database
        showOfflineRowsIfApplicable();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.date_pickers, menu);
        return true;
    }

    /**
     * <h1>onOptionsItemSelected</h1>
     * <p>Handles the click on every option from the menu in the {@link android.support.v7.widget.Toolbar}</p>
     *
     * @param item the clicked {@link MenuItem}
     * @return boolean click state
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Click listener for the start time for the date range
            case R.id.action_start_time:
                final DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        startDate = year + "-" + month + "-" + day;
                    }
                };

                new DatePickerDialog(this, startDateListener, Calendar.YEAR+(2017-Calendar.YEAR),
                        Calendar.MONTH, Calendar.DAY_OF_MONTH).show();

                break;

            // Click listener for the end time for the date range
            case R.id.action_end_time:
                final DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        endDate = year + "-" + month + "-" + day;
                        if (null != startDate && !startDate.trim().isEmpty() && null != endDate && !endDate.trim().isEmpty()) {
                            progressDialog = new ProgressDialog(EarthquakeListActivity.this);
                            progressDialog.setMessage(getString(R.string.msg_loading));
                            progressDialog.show();

                            RetrofitManager.getInstance().getEarthquakes(CodeChallengeConstants.QUERY_FORMAT,
                                    startDate, endDate, EarthquakeListActivity.this);
                        }
                    }
                };

                new DatePickerDialog(this, endDateListener, Calendar.YEAR+(2017-Calendar.YEAR),
                        Calendar.MONTH, Calendar.DAY_OF_MONTH).show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * <h1>onSuccess</h1>
     * <p>Method called when the request is successful.</p>
     *
     * @param pModel the earthquakes
     */
    @Override
    public void onSuccess(EarthquakeModel pModel) {
        // Dismiss the progress dialog.
        if (null != progressDialog && !isFinishing()) {
            progressDialog.dismiss();
        }

        // If there's no earthquakes, a message is shown
        if (pModel.getFeatures().isEmpty()) {
            Toast.makeText(this, R.string.msg_no_quakes, Toast.LENGTH_SHORT).show();
        } else {
            // In this case, there IS earthquakes
            assert mBinding.listInclude.earthquakeList != null;
            setupRecyclerView(mBinding.listInclude.earthquakeList, pModel.getFeatures());

            // Once a search has been made, we save the last search
            Intent serviceIntent = new Intent(this, DatabaseService.class);
            serviceIntent.putExtra(CodeChallengeConstants.EARTHQUAKES_KEY, pModel);
            startService(serviceIntent);
        }
    }

    /**
     * <h1>onFailure</h1>
     * <p>Called when the web service is not available, or something happened with the request.</p>
     */
    @Override
    public void onFailure() {
        if (null != progressDialog && !isFinishing()) {
            progressDialog.dismiss();
        }

        Toast.makeText(this, R.string.err_connection, Toast.LENGTH_SHORT).show();
    }

    /**
     * <h1>showOfflineRowsIfApplicable</h1>
     * <p>Shows the offline data, saved in a SQLite database.</p>
     */
    private void showOfflineRowsIfApplicable() {
        // We check if there's internet connection
        if (!isConnected()) {
            if (EarthquakeDataSource.databaseExists()) {
                EarthquakeDataSource dataSource = new EarthquakeDataSource(this);
                dataSource.open();

                setupRecyclerView(mBinding.listInclude.earthquakeList, dataSource.getEarthquakes()
                        .getFeatures());

                dataSource.close();
                Toast.makeText(this, R.string.msg_showing_offline, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.err_no_offline, Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * <h1>isConnected</h1>
     * <p>Checks the current network state.</p>
     *
     * @return a boolean saying if there's internet or not
     */
    private boolean isConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * <h1>setupRecyclerView</h1>
     * <p>Initializes the {@link RecyclerView}</p>
     *
     * @param recyclerView the {@link RecyclerView} that shows the earthquakes.
     * @param pDataSet     where the earthquakes are.
     */
    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<Feature> pDataSet) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(pDataSet));
        mBinding.listInclude.earthquakeList.setVisibility(View.VISIBLE);
        mBinding.listInclude.txtvInstructions.setVisibility(View.GONE);
    }

    /**
     * <h1>SimpleItemRecyclerViewAdapter</h1>
     * <p>Adapter class for the RecyclerView.</p>
     */
    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        /**
         * Where all the data is.
         */
        private final List<Feature> mDataSet;

        /**
         * <h1>SimpleItemRecyclerViewAdapter</h1>
         * <p>Constructor for the adapter.</p>
         *
         * @param pDataSet the data
         */
        SimpleItemRecyclerViewAdapter(List<Feature> pDataSet) {
            mDataSet = pDataSet;
        }

        /**
         * <h1>onCreateViewHolder</h1>
         * <p>Inflates the row, and adds the slide animation.</p>
         *
         * @param parent   the root view of where the {@link RecyclerView} is
         * @param viewType row type.
         * @return a {@link android.support.v7.widget.RecyclerView.ViewHolder}
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.earthquake_list_content, parent, false);

            Animation animation = AnimationUtils.loadAnimation(EarthquakeListActivity.this, R.anim.push_left_in);
            animation.setDuration(500);
            view.startAnimation(animation);

            return new ViewHolder(view);
        }

        /**
         * <h1>onBindViewHolder</h1>
         * <p>Adds the data to every row.</p>
         *
         * @param holder   the {@link android.support.v7.widget.RecyclerView.ViewHolder} instance
         * @param position position for the dataset.
         */
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mDataSet.get(position);

            holder.txtvLat.setText(String.valueOf(holder.mItem.getGeometry().getCoordinates().get(1)));
            holder.txtvLong.setText(String.valueOf(holder.mItem.getGeometry().getCoordinates().get(0)));
            holder.txtvMagnitude.setText(String.valueOf(holder.mItem.getProperties().getMag()));
            holder.txtvPlace.setText(holder.mItem.getProperties().getPlace());

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
                        intent.putExtra(CodeChallengeConstants.LAT_KEY, holder.mItem.getGeometry().getCoordinates().get(1));
                        intent.putExtra(CodeChallengeConstants.LONG_KEY, holder.mItem.getGeometry().getCoordinates().get(0));

                        context.startActivity(intent);
                    }
                }
            });
        }

        /**
         * <h1>getItemCount</h1>
         * <p>Returns the row count.</p>
         *
         * @return the row count
         */
        @Override
        public int getItemCount() {
            return mDataSet.size();
        }

        /**
         * <h1>ViewHolder</h1>
         * <p>A reflection of the row, to add it to cache.</p>
         */
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
