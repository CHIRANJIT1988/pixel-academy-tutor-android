package pixel.academy.tutor;

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

import pixel.academy.tutor.api.UpdateEducationDetails;
import pixel.academy.tutor.db.SQLiteHelper;
import pixel.academy.tutor.helper.OnTaskCompleted;
import pixel.academy.tutor.model.Education;
import pixel.academy.tutor.session.SessionManager;

/**
 * Created by CHIRANJIT on 11/29/2016.
 */

public class EditEducationFragment extends Fragment implements OnTaskCompleted {

    private EditText edit_school, edit_qualification, edit_division, edit_achievement, edit_qualification_status;
    private TextInputLayout layout_school, layout_qualification, layout_division, layout_qualification_status;

    private Context context;
    private Education educationObj;


    public EditEducationFragment()
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_education, container, false);
        findViewById(rootView);

        if(getActivity().getIntent().getBooleanExtra("EDIT", false))
        {
            this.educationObj = (Education) getActivity().getIntent().getSerializableExtra("EDUCATION_OBJ");
            set_data();
        }

        return rootView;
    }


    private void set_data()
    {
        edit_qualification.setText(educationObj.qualification);
        edit_division.setText(educationObj.division);
        edit_qualification_status.setText(educationObj.qualification_status);
        edit_achievement.setText(educationObj.academic_achievement);
        edit_school.setText(educationObj.college);
    }


    private void findViewById(View view) {

        edit_school = (EditText) view.findViewById(R.id.editScholl);
        edit_qualification = (EditText) view.findViewById(R.id.editQualification);
        edit_division = (EditText) view.findViewById(R.id.editDivision);
        edit_achievement = (EditText) view.findViewById(R.id.editAchievement);
        edit_qualification_status= (EditText) view.findViewById(R.id.editStatus);

        layout_school = (TextInputLayout) view.findViewById(R.id.input_layout_school);
        layout_qualification = (TextInputLayout) view.findViewById(R.id.input_layout_qualification);
        layout_division = (TextInputLayout) view.findViewById(R.id.input_layout_division);
        layout_qualification_status = (TextInputLayout) view.findViewById(R.id.input_layout_status);
    }

    private boolean validateSchool()
    {

        if(edit_school.getText().toString().trim().isEmpty())
        {
            edit_school.setError("Enter School / College / University");
            edit_school.requestFocus();
            return false;
        }

        else
        {
            layout_school.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateQualification()
    {

        if(edit_qualification.getText().toString().trim().isEmpty())
        {
            edit_qualification.setError("Enter Qualification");
            edit_qualification.requestFocus();
            return false;
        }

        else
        {
            layout_qualification.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateDivision()
    {

        if(edit_division.getText().toString().trim().isEmpty())
        {
            edit_division.setError("Enter Division");
            edit_division.requestFocus();
            return false;
        }

        else
        {
            layout_division.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateQualificationStatus()
    {

        if (edit_qualification_status.getText().toString().trim().isEmpty())
        {
            edit_qualification_status.setError("Enter Qualification Status");
            edit_qualification_status.requestFocus();
            return false;
        }

        else {
            layout_qualification_status.setErrorEnabled(false);
        }

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_save:

                if(validateSchool() && validateQualification() && validateDivision() && validateQualificationStatus())
                {
                    this.educationObj = initEducationObject();
                    SQLiteHelper helper = new SQLiteHelper(context);

                    if(!getActivity().getIntent().getBooleanExtra("EDIT", false))
                    {
                        if(helper.insert(this.educationObj))
                        {
                            edit_school.getText().clear();
                            edit_qualification.getText().clear();
                            edit_division.getText().clear();
                            edit_achievement.getText().clear();
                            edit_qualification_status.getText().clear();

                            Education.educations.add(this.educationObj);
                            new UpdateEducationDetails(context, this).execute(new SessionManager(context).getUserId());
                            Toast.makeText(context, "Added Successfully", Toast.LENGTH_LONG).show();
                        }
                    }

                    else
                    {
                        helper.update(this.educationObj);
                        new UpdateEducationDetails(context, this).execute(new SessionManager(context).getUserId());
                        Toast.makeText(context, "Updated Successfully", Toast.LENGTH_LONG).show();
                    }
                }

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private Education initEducationObject()
    {

        if(!getActivity().getIntent().getBooleanExtra("EDIT", false))
        {
            this.educationObj = new Education();
        }

        educationObj.qualification = edit_qualification.getText().toString();
        educationObj.division = edit_division.getText().toString();
        educationObj.qualification_status = edit_qualification_status.getText().toString();
        educationObj.college = edit_school.getText().toString();
        educationObj.academic_achievement = edit_achievement.getText().toString();

        return educationObj;
    }

    @Override
    public void onTaskCompleted(boolean flag, int code, String message)
    {

    }
}