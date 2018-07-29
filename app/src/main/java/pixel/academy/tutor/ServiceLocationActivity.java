package pixel.academy.tutor;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import pixel.academy.tutor.gps.GData;
import pixel.academy.tutor.network.InternetConnectionDetector;
import pixel.academy.tutor.session.SessionManager;

import static pixel.academy.tutor.configuration.Configuration.API_KEY;
import static pixel.academy.tutor.configuration.Configuration.OUT_JSON;
import static pixel.academy.tutor.configuration.Configuration.PLACES_API_BASE;
import static pixel.academy.tutor.configuration.Configuration.TYPE_AUTOCOMPLETE;


public class ServiceLocationActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, OnMapReadyCallback
{

    private int back_pressed = 0;

    private GoogleMap mMap;

    private TextView markerText;

    private LatLng center;
    private LinearLayout markerLayout;

    public static double latitude, longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_location);

        if(!new SessionManager(this).checkLogin())
        {
            finish();
        }

        markerText = (TextView) findViewById(R.id.locationMarkertext);
        markerLayout = (LinearLayout) findViewById(R.id.locationMarker);

        if (!isGooglePlayServicesAvailable())
        {
            finish();
        }

        AutoCompleteTextView edit_search = (AutoCompleteTextView) findViewById(R.id.edit_search);

        edit_search.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item_auto_complete));
        edit_search.setOnItemClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("SERVICE LOCATION");

        ServiceLocationActivity.latitude = getIntent().getDoubleExtra("LATITUDE", 0);
        ServiceLocationActivity.longitude = getIntent().getDoubleExtra("LONGITUDE", 0);

        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap map)
    {
        mMap = map;
        setUpMap(ServiceLocationActivity.latitude, ServiceLocationActivity.longitude);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
            {
                finish();
                break;
            }

            case R.id.action_save:

                Intent intent = new Intent();
                intent.putExtra("LATITUDE", ServiceLocationActivity.latitude);
                intent.putExtra("LONGITUDE", ServiceLocationActivity.longitude);
                setResult(100, intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed()
    {

        super.onBackPressed();

        if(back_pressed == 0)
        {
            back_pressed++;
            Toast.makeText(getApplicationContext(), "Press Back Button again to Exit", Toast.LENGTH_LONG).show();
        }

        else
        {
            finish();
        }
    }


    @Override
    protected void onResume()
    {
        super.onResume();
    }


    private void setUpMap(double latitude, double longitude)
    {

        LatLng position = new LatLng(latitude, longitude);
        MarkerOptions options = new MarkerOptions();
        options.position(position);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(position).zoom(16f).tilt(50).build();

        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.setTrafficEnabled(false);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.clear();

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition arg0)
            {

                center = mMap.getCameraPosition().target;

                ServiceLocationActivity.latitude = center.latitude;
                ServiceLocationActivity.longitude = center.longitude;

                markerText.setText(String.valueOf(" My Location "));
                mMap.clear();
                markerLayout.setVisibility(View.VISIBLE);
            }
        });
    }


    private boolean isGooglePlayServicesAvailable()
    {

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (ConnectionResult.SUCCESS == status)
        {
            return true;
        }

        else
        {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }


    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

            Log.v("location ", "lat: " + latitude + ", long: " + longitude);

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    private class SearchLocationAsync extends AsyncTask<String, Void, String>
    {

        private String location;
        private LatLng p1;

        SearchLocationAsync(String location)
        {
            this.location = location;
        }

        @Override
        protected void onPreExecute()
        {

        }

        @Override
        protected String doInBackground(String... params)
        {

            // Getting reference to the SupportMapFragment
            // Create a new global location parameters object
            LocationRequest mLocationRequest = LocationRequest.create();

            //Set the update interval
            mLocationRequest.setInterval(GData.UPDATE_INTERVAL_IN_MILLISECONDS);

            // Use high accuracy
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            // Set the interval ceiling to one minute
            mLocationRequest.setFastestInterval(GData.FAST_INTERVAL_CEILING_IN_MILLISECONDS);

            // Note that location updates are off until the user turns them on
            // boolean mUpdatesRequested = false;

            p1 = getLocationFromAddress(getApplicationContext(), location);
            return null;
        }

        @Override
        protected void onPostExecute(String result)
        {
            try
            {
                setUpMap(p1.latitude, p1.longitude);
            }

            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
    {

        String location = (String) adapterView.getItemAtPosition(position);

        if(new InternetConnectionDetector(this).isConnected())
        {
            //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            //imm.hideSoftInputFromWindow(footer_main.getWindowToken(), 0);
            new SearchLocationAsync(location).execute();
        }

        else
        {
            Toast.makeText(getApplicationContext(), "Internet Connection Failure", Toast.LENGTH_LONG).show();
        }
    }


    public static ArrayList<String> autocomplete(String input)
    {

        ArrayList<String> resultList = new ArrayList<>();

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();

        try
        {

            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:in");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

            System.out.println("URL: "+url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];

            while ((read = in.read(buff)) != -1)
            {
                jsonResults.append(buff, 0, read);
            }
        }

        catch (MalformedURLException e)
        {
            Log.e("Google Place Error: ", "Error processing Places API URL", e);
            return resultList;
        }

        catch (IOException e)
        {
            Log.e("Google Place Error: ", "Error connecting to Places API", e);
            return resultList;
        }

        finally
        {

            if (conn != null)
            {
                conn.disconnect();
            }
        }

        try
        {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<>(predsJsonArray.length());

            for (int i = 0; i < predsJsonArray.length(); i++)
            {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        }

        catch (JSONException e)
        {
            Log.e("Google Place Error: ", "Cannot process JSON results", e);
        }

        return resultList;
    }


    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable
    {

        private ArrayList<String> resultList;

        GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId)
        {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter()
        {

            Filter filter = new Filter()
            {

                @Override
                protected FilterResults performFiltering(CharSequence constraint)
                {

                    FilterResults filterResults = new FilterResults();

                    if (constraint != null)
                    {

                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }

                    return filterResults;
                }


                @Override
                protected void publishResults(CharSequence constraint, FilterResults results)
                {

                    if (results != null && results.count > 0)
                    {
                        notifyDataSetChanged();
                    }

                    else
                    {
                        notifyDataSetInvalidated();
                    }
                }
            };

            return filter;
        }
    }
}