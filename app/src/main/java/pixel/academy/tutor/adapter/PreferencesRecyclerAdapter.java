package pixel.academy.tutor.adapter;

/**
 * Created by CHIRANJIT on 12/26/2016.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import pixel.academy.tutor.R;
import pixel.academy.tutor.helper.OnTaskCompleted;
import pixel.academy.tutor.model.TutorPreference;


public class PreferencesRecyclerAdapter extends RecyclerView.Adapter<PreferencesRecyclerAdapter.ViewHolder>
{

    private List<TutorPreference> preferences;
    private Context context;
    private OnTaskCompleted listener;
    private OnItemClickListener clickListener;


    public PreferencesRecyclerAdapter(Context context, OnTaskCompleted listener, List<TutorPreference> preferences)
    {
        this.context = context;
        this.listener = listener;
        this.preferences = preferences;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerlist_item_preferences, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder vHolder, int i)
    {
        vHolder.check_applicable.setTag(i);
        vHolder.bindData(preferences.get(i));
    }

    @Override
    public int getItemCount()
    {
        return preferences == null ? 0 : preferences.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tv_title;
        private CheckBox check_applicable;

        private ViewHolder(View itemView)
        {
            super(itemView);

            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            check_applicable = (CheckBox) itemView.findViewById(R.id.check_applicable);

            itemView.setOnClickListener(this);

            check_applicable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton v, boolean isChecked)
                {
                    clickListener.onItemClick(v, getAdapterPosition(), isChecked);
                }
            });
        }

        private void bindData(TutorPreference preference)
        {
            tv_title.setText(preference.preference_name);

            if(preference.status == 1)
            {
                check_applicable.setChecked(true);
            }

            else
            {
                check_applicable.setChecked(false);
            }
        }

        @Override
        public void onClick(View v)
        {
            //clickListener.onItemClick(v, getAdapterPosition(), true);
        }
    }

    public interface OnItemClickListener
    {
        void onItemClick(View view, int position, boolean flag);
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener)
    {
        this.clickListener = itemClickListener;
    }
}