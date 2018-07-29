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

import pixel.academy.tutor.adapter.EducationRecyclerAdapter;
import pixel.academy.tutor.api.UpdateEducationDetails;
import pixel.academy.tutor.db.SQLiteHelper;
import pixel.academy.tutor.helper.OnTaskCompleted;
import pixel.academy.tutor.model.Education;
import pixel.academy.tutor.session.SessionManager;

/**
 * Created by CHIRANJIT on 11/22/2016.
 */
public class EducationFragment extends Fragment implements View.OnClickListener, OnTaskCompleted
{

    private EducationRecyclerAdapter adapter;
    private Context context;

    public EducationFragment()
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
        View rootView = inflater.inflate(R.layout.fragment_education, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        new SQLiteHelper(context).getAllEducation();

        adapter = new EducationRecyclerAdapter(getActivity(), this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.SetOnItemClickListener(new EducationRecyclerAdapter.OnItemClickListener() {

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
                intent.putExtra("FRAGMENT", "EDUCATION");
                intent.putExtra("EDIT", true);
                intent.putExtra("EDUCATION_OBJ", Education.educations.get(code));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

            if(flag && message.equals("delete"))
            {
                Education education = Education.educations.get(code);
                education.status = 0;
                new SQLiteHelper(context).update(education);
                new UpdateEducationDetails(context, this).execute(new SessionManager(context).getUserId());
                Toast.makeText(context, "Education Deleted", Toast.LENGTH_LONG).show();
            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}