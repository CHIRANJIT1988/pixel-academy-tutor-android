package pixel.academy.tutor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import pixel.academy.tutor.adapter.OccupationRecyclerAdapter;
import pixel.academy.tutor.api.UpdateOccupationDetails;
import pixel.academy.tutor.db.SQLiteHelper;
import pixel.academy.tutor.helper.OnTaskCompleted;
import pixel.academy.tutor.model.Occupation;
import pixel.academy.tutor.session.SessionManager;

/**
 * Created by CHIRANJIT on 11/22/2016.
 */
public class OccupationFragment extends Fragment implements View.OnClickListener, OnTaskCompleted
{

    private OccupationRecyclerAdapter adapter;
    private Context context;

    public OccupationFragment()
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
        View rootView = inflater.inflate(R.layout.fragment_occupation, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        new SQLiteHelper(context).getAllOccupation();

        adapter = new OccupationRecyclerAdapter(getActivity(), this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.SetOnItemClickListener(new OccupationRecyclerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position)
            {

            }
        });

        return rootView;
    }

    @Override
    public void onClick(View view)
    {

    }

    @Override
    public void onResume()
    {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onTaskCompleted(boolean flag, int code, String message)
    {

        try
        {
            if(flag && message.equals("edit"))
            {
                Intent intent = new Intent(context, EditProfileActivity.class);
                intent.putExtra("FRAGMENT", "OCCUPATION");
                intent.putExtra("EDIT", true);
                intent.putExtra("OCCUPATION_OBJ", Occupation.occupationList.get(code));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

            if(flag && message.equals("delete"))
            {
                Occupation occupation = Occupation.occupationList.get(code);
                occupation.status = 0;
                new SQLiteHelper(context).update(occupation);
                new UpdateOccupationDetails(context, this).execute(new SessionManager(context).getUserId());
                Toast.makeText(context, "Occupation Deleted", Toast.LENGTH_LONG).show();
            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}