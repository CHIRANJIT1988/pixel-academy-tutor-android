package pixel.academy.tutor;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import pixel.academy.tutor.api.UpdateAddressDetails;
import pixel.academy.tutor.helper.OnTaskCompleted;
import pixel.academy.tutor.model.Address;
import pixel.academy.tutor.session.SessionManager;

import static pixel.academy.tutor.app.Global.ADDRESS;
import static pixel.academy.tutor.app.Global.ADDRESS_DETAILS;
import static pixel.academy.tutor.app.Global.CITY;
import static pixel.academy.tutor.app.Global.COUNTRY;
import static pixel.academy.tutor.app.Global.LOCALITY;
import static pixel.academy.tutor.app.Global.PINCODE;
import static pixel.academy.tutor.app.Global.STATE;
import static pixel.academy.tutor.app.MyApplication.prefs;

/**
 * Created by CHIRANJIT on 11/27/2016.
 */

public class EditAddressFragment extends Fragment implements OnTaskCompleted
{

    private EditText edit_address, edit_locality, edit_city, edit_state, edit_country, edit_pincode;
    private TextInputLayout layout_address, layout_locality, layout_city, layout_state, layout_country, layout_pincode;

    private Context context;
    private ProgressDialog pDialog;
    private SessionManager session;

    private static String json_data;


    public EditAddressFragment()
    {

    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        this.context = getActivity();
        this.pDialog = new ProgressDialog(context);
        this.session = new SessionManager(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_edit_address, container, false);
        findViewById(rootView);
        setProfileData(prefs.getString(ADDRESS_DETAILS, ""));
        return rootView;
    }


    private void initProgressDialog(String message)
    {
        pDialog.setMessage(message);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(false);
        pDialog.show();
    }

    private void findViewById(View view)
    {

        edit_address = (EditText) view.findViewById(R.id.editAddress);
        edit_locality = (EditText) view.findViewById(R.id.editLocality);
        edit_city = (EditText) view.findViewById(R.id.editCity);
        edit_state = (EditText) view.findViewById(R.id.editState);
        edit_country = (EditText) view.findViewById(R.id.editCountry);
        edit_pincode = (EditText) view.findViewById(R.id.editPincode);

        layout_address = (TextInputLayout) view.findViewById(R.id.input_layout_address);
        layout_locality = (TextInputLayout) view.findViewById(R.id.input_layout_locality);
        layout_city = (TextInputLayout) view.findViewById(R.id.input_layout_city);
        layout_state = (TextInputLayout) view.findViewById(R.id.input_layout_state);
        layout_country = (TextInputLayout) view.findViewById(R.id.input_layout_country);
        layout_pincode = (TextInputLayout) view.findViewById(R.id.input_layout_pincode);
    }

    private void setProfileData(String profile_data)
    {
        try
        {
            JSONObject jsonObj = new JSONObject(profile_data);

            if (jsonObj.has(ADDRESS))
            {
                edit_address.setText(jsonObj.getString(ADDRESS));
            }

            if (jsonObj.has(LOCALITY))
            {
                edit_locality.setText(jsonObj.getString(LOCALITY));
            }

            if (jsonObj.has(CITY))
            {
                edit_city.setText(jsonObj.getString(CITY));
            }

            if (jsonObj.has(STATE))
            {
                edit_state.setText(jsonObj.getString(STATE));
            }

            if (jsonObj.has(COUNTRY))
            {
                edit_country.setText(jsonObj.getString(COUNTRY));
            }

            if (jsonObj.has(PINCODE))
            {
                edit_pincode.setText(jsonObj.getString(PINCODE));
            }
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    private Address initAddressObject()
    {

        Address address = new Address();

        address.home_address = edit_address.getText().toString();
        address.locality = edit_locality.getText().toString();
        address.city = edit_city.getText().toString();
        address.state = edit_state.getText().toString();
        address.country = edit_country.getText().toString();
        address.pincode = edit_pincode.getText().toString();

        return address;
    }


    private String buildJSONData(Address address)
    {

        JSONObject jsonObject = new JSONObject();

        try
        {
            jsonObject.put(ADDRESS, address.home_address);
            jsonObject.put(LOCALITY, address.locality);
            jsonObject.put(CITY, address.city);
            jsonObject.put(STATE, address.state);
            jsonObject.put(COUNTRY, address.country);
            jsonObject.put(PINCODE, address.pincode);
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_profile, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.action_save:

                /*if (!new InternetConnectionDetector(this).isConnected())
                {
                    Toast.makeText(getApplicationContext(), "Internet Connection Failure", Toast.LENGTH_LONG).show();
                    return false;
                }*/

                if(validateAddress() && validateLocality() && validateCity() && validateState() && validateCountry() && validatePincode())
                {
                    initProgressDialog("Updating Profile ...");
                    json_data = buildJSONData(initAddressObject());
                    new UpdateAddressDetails(context, this).execute(json_data, session.getUserId());
                }

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean validateAddress()
    {

        if(edit_address.getText().toString().trim().isEmpty())
        {
            edit_address.setError("Enter Home Address");
            edit_address.requestFocus();
            return false;
        }

        else
        {
            layout_address.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validateLocality()
    {

        if(edit_locality.getText().toString().trim().isEmpty())
        {
            edit_locality.setError("Enter Locality");
            edit_locality.requestFocus();
            return false;
        }

        else
        {
            layout_locality.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validateCity()
    {

        if(edit_city.getText().toString().trim().isEmpty())
        {
            edit_city.setError("Enter City");
            edit_city.requestFocus();
            return false;
        }

        else
        {
            layout_city.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validateState()
    {

        if(edit_state.getText().toString().trim().isEmpty())
        {
            edit_state.setError("Enter State");
            edit_state.requestFocus();
            return false;
        }

        else
        {
            layout_state.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validateCountry()
    {

        if(edit_country.getText().toString().trim().isEmpty())
        {
            edit_country.setError("Enter Country");
            edit_country.requestFocus();
            return false;
        }

        else
        {
            layout_country.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validatePincode()
    {

        if(edit_pincode.getText().toString().trim().length() != 6)
        {
            edit_pincode.setError("Invalid Pincode");
            edit_pincode.requestFocus();
            return false;
        }

        else
        {
            layout_pincode.setErrorEnabled(false);
        }

        return true;
    }

    @Override
    public void onTaskCompleted(boolean flag, int code, String message)
    {
        try
        {
            if(code == 200)
            {
                prefs.edit().putString(ADDRESS_DETAILS, json_data).apply();
            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

        finally
        {
            if(pDialog.isShowing())
            {
                pDialog.dismiss();
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}