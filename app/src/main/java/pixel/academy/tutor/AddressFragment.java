package pixel.academy.tutor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import static pixel.academy.tutor.app.Global.ADDRESS;
import static pixel.academy.tutor.app.Global.ADDRESS_DETAILS;
import static pixel.academy.tutor.app.Global.CITY;
import static pixel.academy.tutor.app.Global.COUNTRY;
import static pixel.academy.tutor.app.Global.LOCALITY;
import static pixel.academy.tutor.app.Global.PINCODE;
import static pixel.academy.tutor.app.Global.STATE;
import static pixel.academy.tutor.app.MyApplication.prefs;
import static pixel.academy.tutor.service.NextButtonBroadcast.sendToBroadcast;

/**
 * Created by CHIRANJIT on 11/22/2016.
 */
public class AddressFragment extends Fragment implements View.OnClickListener
{

    private Context context;

    private ImageButton ib_edit;
    private TextView tv_address, tv_locality, tv_city, tv_state, tv_country, tv_pincode;

    public AddressFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_address, container, false);
        findViewById(rootView);
        ib_edit.setOnClickListener(this);
        return rootView;
    }

    private void findViewById(View view)
    {
        ib_edit = (ImageButton) view.findViewById(R.id.ibEdit);

        tv_address = (TextView) view.findViewById(R.id.address);
        tv_locality = (TextView) view.findViewById(R.id.locality);
        tv_city = (TextView) view.findViewById(R.id.city);
        tv_state = (TextView) view.findViewById(R.id.state);
        tv_country = (TextView) view.findViewById(R.id.country);
        tv_pincode = (TextView) view.findViewById(R.id.pincode);
    }


    @Override
    public void onResume()
    {
        super.onResume();
        setProfileData(prefs.getString(ADDRESS_DETAILS, ""));
    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.ibEdit:

                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtra("INDEX", 0);
                startActivity(intent);
                break;

        }
    }

    private void setProfileData(String address_json_data)
    {

        try
        {

            JSONObject jsonObj = new JSONObject(address_json_data);

            if (jsonObj.has(ADDRESS))
            {
                tv_address.setText(jsonObj.getString(ADDRESS));
            }

            if (jsonObj.has(LOCALITY))
            {
                tv_locality.setText(jsonObj.getString(LOCALITY));
            }

            if (jsonObj.has(CITY))
            {
                tv_city.setText(jsonObj.getString(CITY));
            }

            if (jsonObj.has(STATE))
            {
                tv_state.setText(jsonObj.getString(STATE));
            }

            if (jsonObj.has(COUNTRY))
            {
                tv_country.setText(jsonObj.getString(COUNTRY));
            }

            if (jsonObj.has(PINCODE))
            {
                tv_pincode.setText(jsonObj.getString(PINCODE));
            }
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
