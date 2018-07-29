package pixel.academy.tutor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import pixel.academy.tutor.session.SessionManager;

import static pixel.academy.tutor.service.NextButtonBroadcast.sendToBroadcast;

/**
 * Created by CHIRANJIT on 11/22/2016.
 */
public class AccountFragment extends Fragment implements View.OnClickListener{

    private ImageButton ib_edit1, ib_edit2;

    private Context context;
    private ProgressDialog pDialog;
    private SessionManager session;

    public AccountFragment()
    {

    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.context = getActivity();
        this.pDialog = new ProgressDialog(context);
        this.session = new SessionManager(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);
        findViewById(rootView);
        setListener();
        return rootView;
    }


    private void initProgressDialog(String message) {

        pDialog.setMessage(message);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(false);
        pDialog.show();
    }

    private void setListener()
    {
        ib_edit1.setOnClickListener(this);
        ib_edit2.setOnClickListener(this);
    }


    private void findViewById(View view)
    {
        ib_edit1 = (ImageButton) view.findViewById(R.id.ibEdit1);
        ib_edit2 = (ImageButton) view.findViewById(R.id.ibEdit2);
    }


    @Override
    public void onClick(View view)
    {

        switch (view.getId())
        {
            case R.id.ibEdit2:

                Intent intent2 = new Intent(getActivity(), EditProfileActivity.class);
                intent2.putExtra("INDEX", 2);
                startActivity(intent2);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}