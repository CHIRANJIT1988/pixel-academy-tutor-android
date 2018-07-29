package pixel.academy.tutor;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pixel.academy.tutor.adapter.PreferencesRecyclerAdapter;
import pixel.academy.tutor.api.UpdatePreferenceDetails;
import pixel.academy.tutor.db.SQLiteHelper;
import pixel.academy.tutor.helper.OnTaskCompleted;
import pixel.academy.tutor.model.Board;
import pixel.academy.tutor.model.FeesRange;
import pixel.academy.tutor.model.Medium;
import pixel.academy.tutor.model.Qualification;
import pixel.academy.tutor.model.Standard;
import pixel.academy.tutor.model.Subject;
import pixel.academy.tutor.model.TutorPreference;
import pixel.academy.tutor.network.InternetConnectionDetector;
import pixel.academy.tutor.session.SessionManager;

import static pixel.academy.tutor.app.Global.TABLE_MASTER_BOARD;
import static pixel.academy.tutor.app.Global.TABLE_MASTER_CLASS;
import static pixel.academy.tutor.app.Global.TABLE_MASTER_FEES_RANGE;
import static pixel.academy.tutor.app.Global.TABLE_MASTER_MEDIUM;
import static pixel.academy.tutor.app.Global.TABLE_MASTER_QUALIFICATION;
import static pixel.academy.tutor.app.Global.TABLE_MASTER_SUBJECT;

/**
 * Created by CHIRANJIT on 12/26/2016.
 */
public class EditPreferenceFragment extends Fragment implements OnTaskCompleted {

    private Context context;
    private ProgressDialog pDialog;
    private SessionManager session;

    private List<TutorPreference> qualification_list = new ArrayList<>();
    private List<TutorPreference> medium_list = new ArrayList<>();
    private List<TutorPreference> board_list = new ArrayList<>();
    private List<TutorPreference> subject_list = new ArrayList<>();
    private List<TutorPreference> fees_range_list = new ArrayList<>();
    private List<TutorPreference> class_list = new ArrayList<>();

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
        View rootView = inflater.inflate(R.layout.fragment_edit_preference, container, false);

        qualification(rootView);
        board(rootView);
        standard(rootView);
        medium(rootView);
        fees_range(rootView);
        subject(rootView);

        return rootView;
    }

    private void qualification(View view)
    {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_qualification);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        for(Qualification q: new SQLiteHelper(context).getAllQualificationMaster())
        {
            qualification_list.add(new TutorPreference(q.id, q.qualification, q.status));
        }

        PreferencesRecyclerAdapter adapter = new PreferencesRecyclerAdapter(getActivity(), this, qualification_list);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.SetOnItemClickListener(new PreferencesRecyclerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position, boolean isChecked)
            {
                if(isChecked)
                {
                    qualification_list.get(position).status = 1;
                }

                else
                {
                    qualification_list.get(position).status = 0;
                }
            }
        });
    }

    private void medium(View view)
    {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_medium);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        for(Medium m: new SQLiteHelper(context).getAllMediumMaster())
        {
            medium_list.add(new TutorPreference(m.id, m.medium, m.status));
        }

        PreferencesRecyclerAdapter adapter = new PreferencesRecyclerAdapter(getActivity(), this, medium_list);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.SetOnItemClickListener(new PreferencesRecyclerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position, boolean isChecked)
            {
                if(isChecked)
                {
                    medium_list.get(position).status = 1;
                }

                else
                {
                    medium_list.get(position).status = 0;
                }
            }
        });
    }

    private void board(View view)
    {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_board);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        for(Board b: new SQLiteHelper(context).getAllBoardMaster())
        {
            board_list.add(new TutorPreference(b.id, b.board, b.status));
        }

        PreferencesRecyclerAdapter adapter = new PreferencesRecyclerAdapter(getActivity(), this, board_list);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.SetOnItemClickListener(new PreferencesRecyclerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position, boolean isChecked)
            {
                if(isChecked)
                {
                    board_list.get(position).status = 1;
                }

                else
                {
                    board_list.get(position).status = 0;
                }
            }
        });
    }

    private void subject(View view)
    {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_subject);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        for(Subject s: new SQLiteHelper(context).getAllSubjectMaster())
        {
            subject_list.add(new TutorPreference(s.id, s.subject, s.status));
        }

        PreferencesRecyclerAdapter adapter = new PreferencesRecyclerAdapter(getActivity(), this, subject_list);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.SetOnItemClickListener(new PreferencesRecyclerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position, boolean isChecked)
            {
                if(isChecked)
                {
                    subject_list.get(position).status = 1;
                }

                else
                {
                    subject_list.get(position).status = 0;
                }
            }
        });
    }

    private void fees_range(View view)
    {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_fees_range);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        for(FeesRange range: new SQLiteHelper(context).getAllFeesRangeMaster())
        {
            fees_range_list.add(new TutorPreference(range.id, range.fees_range, range.status));
        }

        PreferencesRecyclerAdapter adapter = new PreferencesRecyclerAdapter(getActivity(), this, fees_range_list);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.SetOnItemClickListener(new PreferencesRecyclerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position, boolean isChecked)
            {
                if(isChecked)
                {
                    fees_range_list.get(position).status = 1;
                }

                else
                {
                    fees_range_list.get(position).status = 0;
                }
            }
        });
    }

    private void standard(View view)
    {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_class);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        for(Standard s: new SQLiteHelper(context).getAllClassMaster())
        {
            class_list.add(new TutorPreference(s._id, s._class, s._status));
        }

        PreferencesRecyclerAdapter adapter = new PreferencesRecyclerAdapter(getActivity(), this, class_list);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.SetOnItemClickListener(new PreferencesRecyclerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position, boolean isChecked)
            {
                if(isChecked)
                {
                    class_list.get(position).status = 1;
                }

                else
                {
                    class_list.get(position).status = 0;
                }
            }
        });
    }

    private void save_data()
    {
        SQLiteHelper helper = new SQLiteHelper(context);

        for(TutorPreference p: qualification_list)
        {
            helper.update_master(TABLE_MASTER_QUALIFICATION, p.id, p.status);
        }

        for(TutorPreference p: medium_list)
        {
            helper.update_master(TABLE_MASTER_MEDIUM, p.id, p.status);
        }

        for(TutorPreference p: subject_list)
        {
            helper.update_master(TABLE_MASTER_SUBJECT, p.id, p.status);
        }

        for(TutorPreference p: class_list)
        {
            helper.update_master(TABLE_MASTER_CLASS, p.id, p.status);
        }

        for(TutorPreference p: board_list)
        {
            helper.update_master(TABLE_MASTER_BOARD, p.id, p.status);
        }

        for(TutorPreference p: fees_range_list)
        {
            helper.update_master(TABLE_MASTER_FEES_RANGE, p.id, p.status);
        }
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

                send_data();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initProgressDialog(String message)
    {
        pDialog.setMessage(message);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(false);
        pDialog.show();
    }

    private void send_data()
    {
        if (!new InternetConnectionDetector(context).isConnected())
        {
            Toast.makeText(context, "Internet Connection Failure", Toast.LENGTH_LONG).show();
            return;
        }

        initProgressDialog("Updating Profile ...");
        String json_data = TutorPreference.buildJSONData(medium_list, board_list, class_list, subject_list);
        new UpdatePreferenceDetails(context, this).execute(json_data, session.getUserId());
    }

    @Override
    public void onTaskCompleted(boolean flag, int code, String message)
    {
        try
        {
            if(code == 200)
            {
                save_data();
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