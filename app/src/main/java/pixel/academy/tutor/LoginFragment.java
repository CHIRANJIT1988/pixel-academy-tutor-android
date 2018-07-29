package pixel.academy.tutor;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import pixel.academy.tutor.helper.OnTaskCompleted;
import pixel.academy.tutor.model.User;
import pixel.academy.tutor.api.LoginUser;
import pixel.academy.tutor.network.InternetConnectionDetector;

import static pixel.academy.tutor.RegisterActivity.user;
import static pixel.academy.tutor.app.MyApplication.prefs;
import static pixel.academy.tutor.app.Global.TOKEN;


public class LoginFragment extends Fragment implements OnTaskCompleted, View.OnClickListener
{

    private TextInputLayout layout_mobile_number, layout_password;
    private Button btnNewPassword, btnLogin;
    private EditText editPhone, editPassword;
    private TextView tvStatus;
    private ProgressBar pBar;

    private RelativeLayout relative_main;
    private Context context = null;


    public LoginFragment()
    {

    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        context = this.getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        findViewById(rootView);
        addClickListener();

        hideKeyboard(rootView);

        return rootView;
    }


    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }


    @Override
    public void onDetach()
    {
        super.onDetach();
    }


    private void findViewById(View rootView)
    {

        layout_mobile_number = (TextInputLayout) rootView.findViewById(R.id.input_layout_phone_number);
        layout_password = (TextInputLayout)  rootView.findViewById(R.id.input_layout_password);

        btnNewPassword = (Button) rootView.findViewById(R.id.btnNewPassword);
        btnLogin = (Button) rootView.findViewById(R.id.btnLogin);

        editPhone = (EditText) rootView.findViewById(R.id.editPhoneNumber);
        editPassword = (EditText) rootView.findViewById(R.id.editPassword);

        pBar = (ProgressBar) rootView.findViewById(R.id.pbLoading);
        tvStatus = (TextView) rootView.findViewById(R.id.status);
        relative_main = (RelativeLayout) rootView.findViewById(R.id.relative_main);
    }


    private void addClickListener()
    {
        btnLogin.setOnClickListener(this);
        btnNewPassword.setOnClickListener(this);
    }


    /** Called just before the activity is destroyed. */
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.d("Inside : ", "onDestroy() event");
    }


    @Override
    public void onClick(View view)
    {

        switch (view.getId())
        {

            /*case R.id.btnForgotPassword:

                Fragment fragment1 = new ForgotPasswordFragment();

                FragmentManager fragmentManager1 = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();

                fragmentTransaction1.setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim);
                fragmentTransaction1.replace(R.id.container_body, fragment1);
                fragmentTransaction1.commit();

                getActivity().setTitle("FORGOT PASSWORD");

                break;*/

            case R.id.btnLogin:

                if(validateMobileNumber() && validatePassword())
                {
                    if (!new InternetConnectionDetector(getActivity()).isConnected())
                    {
                        makeSnackbar("Internet Connection Fail");
                        return;
                    }

                    pBar.setVisibility(View.VISIBLE);
                    tvStatus.setText(String.valueOf("Logging ... "));

                    user = initUserObject();
                    new LoginUser(context, LoginFragment.this).execute(user);
                }

                break;
        }
    }

    private User initUserObject()
    {
        WifiManager m_wm = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);

        User user = new User();

        user.setPhoneNumber(editPhone.getText().toString());
        user.setPassword(editPassword.getText().toString());
        user.fcm_reg_id = prefs.getString(TOKEN, "");
        user.setDeviceID(String.valueOf(m_wm.getConnectionInfo().getMacAddress()));

        return user;
    }


    private boolean validateMobileNumber()
    {
        if(editPhone.getText().toString().trim().length() != 10)
        {
            editPhone.setError("Invalid Mobile Number");
            editPhone.requestFocus();
            return false;
        }

        else
        {
            layout_mobile_number.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validatePassword()
    {
        if(editPassword.getText().toString().trim().isEmpty())
        {
            editPassword.setError("Enter Password");
            editPassword.requestFocus();
            return false;
        }

        else
        {
            layout_password.setErrorEnabled(false);
        }

        return true;
    }


    private void makeSnackbar(String msg)
    {
        Snackbar snackbar = Snackbar.make(relative_main, msg, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.myPrimaryColor));
        snackbar.show();
    }

    private void hideKeyboard(final View rootView)
    {

        editPhone.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (editPhone.getText().toString().trim().length() == 10) {
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
                }
            }
        });
    }

    @Override
    public void onTaskCompleted(boolean flag, int code, String message)
    {
        try
        {
            if (flag && code == 200)
            {
                Intent callIntent = new Intent(context, ProfileActivity.class);
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(callIntent);
                getActivity().finish();
            }

            else
            {
                tvStatus.setText("");
                pBar.setVisibility(View.GONE);
                makeSnackbar(message);
            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}