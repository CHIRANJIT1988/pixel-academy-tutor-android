package pixel.academy.tutor;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import pixel.academy.tutor.api.UpdateOccupationDetails;
import pixel.academy.tutor.db.SQLiteHelper;
import pixel.academy.tutor.helper.DatePickerFragment;
import pixel.academy.tutor.helper.Helper;
import pixel.academy.tutor.helper.OnTaskCompleted;
import pixel.academy.tutor.model.Occupation;
import pixel.academy.tutor.session.SessionManager;

/**
 * Created by CHIRANJIT on 11/29/2016.
 */
public class EditOccupationFragment extends Fragment implements OnTaskCompleted {

    private EditText edit_organization, edit_occupation, edit_start_date, edit_end_date;
    private TextInputLayout layout_organization, layout_occupation;

    private Context context;

    private static int DATE_PICKER = 0;
    private Occupation occupationObj;


    public EditOccupationFragment()
    {

    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        this.context = getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.fragment_edit_occupation, container, false);
        findViewById(rootView);

        edit_start_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    DATE_PICKER = 1;
                    showDatePicker();
                }
            }
        });

        edit_end_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    DATE_PICKER = 2;
                    showDatePicker();
                }
            }
        });

        if(getActivity().getIntent().getBooleanExtra("EDIT", false))
        {
            this.occupationObj = (Occupation) getActivity().getIntent().getSerializableExtra("OCCUPATION_OBJ");
            set_data();
        }

        return rootView;
    }

    private void set_data()
    {
        edit_occupation.setText(occupationObj.occupation);
        edit_organization.setText(occupationObj.organization);
        edit_start_date.setText(occupationObj.start_date);
        edit_end_date.setText(occupationObj.end_date);
    }

    private void findViewById(View view)
    {
        edit_organization = (EditText) view.findViewById(R.id.editOrganization);
        edit_occupation = (EditText) view.findViewById(R.id.editOccupation);
        edit_start_date = (EditText) view.findViewById(R.id.editStartDate);
        edit_end_date = (EditText) view.findViewById(R.id.editEndDate);

        layout_organization = (TextInputLayout) view.findViewById(R.id.input_layout_organization);
        layout_occupation = (TextInputLayout) view.findViewById(R.id.input_layout_occupation);
    }


    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_profile, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    private boolean validateOccupation()
    {

        if(edit_occupation.getText().toString().trim().isEmpty())
        {
            edit_occupation.setError("Enter Occupation");
            edit_occupation.requestFocus();
            return false;
        }

        else
        {
            layout_occupation.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateOrganization()
    {

        if(edit_organization.getText().toString().trim().isEmpty())
        {
            edit_organization.setError("Enter Organization");
            edit_organization.requestFocus();
            return false;
        }

        else
        {
            layout_organization.setErrorEnabled(false);
        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_save:

                if(validateOccupation() && validateOrganization())
                {
                    this.occupationObj = initOccupationObject();
                    SQLiteHelper helper = new SQLiteHelper(context);

                    if(!getActivity().getIntent().getBooleanExtra("EDIT", false))
                    {
                        if(helper.insert(this.occupationObj))
                        {
                            clearField();
                            Occupation.occupationList.add(this.occupationObj);

                            new UpdateOccupationDetails(context, this).execute(new SessionManager(context).getUserId());
                            Toast.makeText(context, "Added Successfully", Toast.LENGTH_LONG).show();
                        }
                    }

                    else
                    {
                        clearField();
                        helper.update(this.occupationObj);

                        new UpdateOccupationDetails(context, this).execute(new SessionManager(context).getUserId());
                        Toast.makeText(context, "Updated Successfully", Toast.LENGTH_LONG).show();
                    }
                }

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void clearField()
    {
        edit_occupation.getText().clear();
        edit_organization.getText().clear();
        edit_start_date.getText().clear();
        edit_end_date.getText().clear();
    }

    private Occupation initOccupationObject()
    {

        if(!getActivity().getIntent().getBooleanExtra("EDIT", false))
        {
            this.occupationObj = new Occupation();
        }

        occupationObj.organization = edit_organization.getText().toString();
        occupationObj.occupation = edit_occupation.getText().toString();
        occupationObj.start_date = edit_start_date.getText().toString();
        occupationObj.end_date = edit_end_date.getText().toString();

        return occupationObj;
    }


    private void showDatePicker()
    {
        DatePickerFragment date = new DatePickerFragment();

        Calendar calender = Calendar.getInstance();

        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);

        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Select Date of Birth");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {

            Calendar now = Calendar.getInstance();

            Calendar choosen = Calendar.getInstance();
            choosen.set(year, monthOfYear, dayOfMonth);

            if (choosen.compareTo(now) > 0)
            {
                Toast.makeText(getActivity(), "Invalid Selection", Toast.LENGTH_LONG).show();
                return;
            }


            // Initialize date variable to current date
            String dob = new StringBuilder().append(Helper.format_date(year)).append("-").append(Helper.format_date(monthOfYear + 1)).append("-").append(dayOfMonth).toString();

            if(DATE_PICKER == 1)
            {
                edit_start_date.setText(dob);
            }

            else if(DATE_PICKER == 2)
            {
                edit_end_date.setText(dob);
            }
        }
    };

    @Override
    public void onTaskCompleted(boolean flag, int code, String message)
    {

    }
}