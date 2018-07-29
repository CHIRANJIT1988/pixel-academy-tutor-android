package pixel.academy.tutor;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

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

import pixel.academy.tutor.api.ReceiveBoardMaster;
import pixel.academy.tutor.api.ReceiveClassMaster;
import pixel.academy.tutor.api.ReceiveFeesRangeMaster;
import pixel.academy.tutor.api.ReceiveMediumMaster;
import pixel.academy.tutor.api.ReceiveQualificationMaster;
import pixel.academy.tutor.api.ReceiveSubjectMaster;
import pixel.academy.tutor.gps.GData;
import pixel.academy.tutor.helper.OnTaskCompleted;
import pixel.academy.tutor.network.InternetConnectionDetector;
import pixel.academy.tutor.session.SessionManager;

import static pixel.academy.tutor.app.Global.FIRST_RUN;
import static pixel.academy.tutor.app.MyApplication.prefs;
import static pixel.academy.tutor.configuration.Configuration.API_KEY;
import static pixel.academy.tutor.configuration.Configuration.OUT_JSON;
import static pixel.academy.tutor.configuration.Configuration.PLACES_API_BASE;
import static pixel.academy.tutor.configuration.Configuration.TYPE_AUTOCOMPLETE;


public class ProfileActivity extends AppCompatActivity implements OnTaskCompleted,
        AdapterView.OnItemClickListener, OnMapReadyCallback, View.OnClickListener
{

    private GoogleMap mMap;
    private ViewPager mViewPager;
    private AutoCompleteTextView edit_search;

    private static double latitude, longitude;
    private static String json_data;

    private FloatingActionButton fab;
    private TabLayout tabLayout;
    private SessionManager session; // Session Manager Class

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //this.pDialog = new ProgressDialog(this);
        this.session = new SessionManager(this);

        edit_search = (AutoCompleteTextView) findViewById(R.id.edit_search);

        edit_search.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item_auto_complete));
        edit_search.setOnItemClickListener(this);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        // Fixes bug for disappearing fragment content
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        setupViewPager(mViewPager);

        tabLayout.setupWithViewPager(mViewPager);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        List<String> icons = new ArrayList<>();
        icons.add("ic_account_white_24dp");
        icons.add("ic_home_map_marker_white_24dp");
        icons.add("ic_briefcase_white_24dp");
        icons.add("ic_school_white_24dp");

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setIcon(this.getResources().getIdentifier(icons.get(i), "drawable", this.getPackageName()));
        }

        tabLayout.addOnTabSelectedListener (new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());

                if(tab.getPosition() == 2 || tab.getPosition() == 3)
                {
                    fab.setVisibility(View.VISIBLE);
                }
            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                fab.setVisibility(View.GONE);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if (prefs.getBoolean(FIRST_RUN, true))
        {
            prefs.edit().putBoolean(FIRST_RUN, false).apply();
        }

        if(!session.checkLogin())
        {
            finish();
            return;
        }

        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);

        new ReceiveQualificationMaster(getApplicationContext()).execute();
        new ReceiveSubjectMaster(getApplicationContext()).execute();
        new ReceiveMediumMaster(getApplicationContext()).execute();
        new ReceiveBoardMaster(getApplicationContext()).execute();
        new ReceiveFeesRangeMaster(getApplicationContext()).execute();
        new ReceiveClassMaster(getApplicationContext()).execute();
    }

    private void setupViewPager(ViewPager viewPager)
    {

        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());

        adapter.addFrag(new AccountFragment(), "My Account");
        adapter.addFrag(new AddressFragment(), "Address");
        adapter.addFrag(new OccupationFragment(), "Occupation");
        adapter.addFrag(new EducationFragment(), "Education");

        viewPager.setAdapter(adapter);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount()
        {
            return mFragmentList.size();
        }

        void addFrag(Fragment fragment, String title)
        {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        /*@Override
        public CharSequence getPageTitle(int position)
        {
            return mFragmentTitleList.get(position);
        }*/
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.fab:

                if(tabLayout.getSelectedTabPosition() == 2)
                {
                    Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                    intent.putExtra("FRAGMENT", "OCCUPATION");
                    startActivity(intent);
                }

                if(tabLayout.getSelectedTabPosition() == 3)
                {
                    Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                    intent.putExtra("FRAGMENT", "EDUCATION");
                    startActivity(intent);
                }

                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap map)
    {
        mMap = map;

        try
        {
            JSONObject jsonObj = new JSONObject(prefs.getString("location_details", ""));

            if (jsonObj.has("latitude") && jsonObj.has("longitude"))
            {
                setUpMap(jsonObj.getDouble("latitude"), jsonObj.getDouble("longitude"));
            }
        }

        catch (JSONException e)
        {
            e.printStackTrace();
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

        mMap.clear();

        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.setTrafficEnabled(false);
        mMap.addMarker(new MarkerOptions().position(position).title("My Location"));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                Intent intent = new Intent(ProfileActivity.this, ServiceLocationActivity.class);
                intent.putExtra("LATITUDE", point.latitude);
                intent.putExtra("LONGITUDE", point.longitude);
                startActivityForResult(intent, 100);
            }
        });
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
                Intent intent = new Intent(ProfileActivity.this, ServiceLocationActivity.class);
                intent.putExtra("LATITUDE", p1.latitude);
                intent.putExtra("LONGITUDE", p1.longitude);
                startActivityForResult(intent, 100);
            }

            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && resultCode == 100)
        {
            latitude = data.getDoubleExtra("LATITUDE", 0);
            longitude = data.getDoubleExtra("LONGITUDE", 0);

            json_data = buildJSONData(latitude, longitude);
            onTaskCompleted(true, 200, "Success");
        }
    }

    private String buildJSONData(double latitude, double longitude)
    {

        JSONObject jsonObject = new JSONObject();

        try
        {
            jsonObject.put("latitude", latitude);
            jsonObject.put("longitude", longitude);
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    @Override
    public void onTaskCompleted(boolean flag, int code, String message)
    {

        try
        {
            if(code == 200)
            {
                prefs.edit().putString("location_details", json_data).apply();
                setUpMap(latitude, longitude);
            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

        finally
        {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
    {

        String location = (String) adapterView.getItemAtPosition(position);

        if(new InternetConnectionDetector(this).isConnected())
        {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edit_search.getWindowToken(), 0);
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