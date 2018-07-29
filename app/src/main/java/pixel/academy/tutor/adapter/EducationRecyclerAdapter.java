package pixel.academy.tutor.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import pixel.academy.tutor.EditProfileActivity;
import pixel.academy.tutor.R;
import pixel.academy.tutor.helper.Helper;
import pixel.academy.tutor.helper.OnTaskCompleted;
import pixel.academy.tutor.model.Education;


public class EducationRecyclerAdapter extends RecyclerView.Adapter<EducationRecyclerAdapter.VersionViewHolder>
{

    private Context context;
    private OnItemClickListener clickListener;
    private OnTaskCompleted listener;


    public EducationRecyclerAdapter(Context context, OnTaskCompleted listener)
    {
        this.context = context;
        this.listener = listener;
    }


    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerlist_item_educations, viewGroup, false);
        return new VersionViewHolder(view);
    }


    @Override
    public void onBindViewHolder(VersionViewHolder versionViewHolder, int i)
    {

        Education education = Education.educations.get(i);

        versionViewHolder.school_name.setText(education.college.toUpperCase());
        versionViewHolder.qualification.setText(education.qualification.toUpperCase());
        versionViewHolder.division.setText(Helper.toCamelCase(education.division));
        versionViewHolder.achievement.setText(education.academic_achievement.toLowerCase());
        versionViewHolder.qualification_status.setText(Helper.toCamelCase(education.qualification_status));

        versionViewHolder.btnDelete.setTag(i);
        versionViewHolder.btnEdit.setTag(i);
    }


    @Override
    public int getItemCount()
    {
        return Education.educations == null ? 0 : Education.educations.size();
    }


    class VersionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView school_name;
        TextView qualification;
        TextView achievement;
        TextView division;
        TextView qualification_status;
        Button btnEdit;
        Button btnDelete;


        VersionViewHolder(View itemView)
        {

            super(itemView);

            school_name = (TextView) itemView.findViewById(R.id.school_name);
            qualification = (TextView) itemView.findViewById(R.id.qualification);
            achievement = (TextView) itemView.findViewById(R.id.achievement);
            division = (TextView) itemView.findViewById(R.id.division);
            qualification_status = (TextView) itemView.findViewById(R.id.status);
            btnEdit = (Button) itemView.findViewById(R.id.btnEdit);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);

            btnDelete.setOnClickListener(onButtonClickListener);
            btnEdit.setOnClickListener(onButtonClickListener);
            itemView.setOnClickListener(this);
        }


        private View.OnClickListener onButtonClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {

                if (v.getId() == R.id.btnEdit)
                {
                    listener.onTaskCompleted(true, (int) v.getTag(), "edit");
                }

                if (v.getId() == R.id.btnDelete)
                {
                    listener.onTaskCompleted(true, (int) v.getTag(), "delete");
                }
            }
        };


        @Override
        public void onClick(View v)
        {
            clickListener.onItemClick(v, getAdapterPosition());
        }
    }


    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
    }


    public void SetOnItemClickListener(final OnItemClickListener itemClickListener)
    {
        this.clickListener = itemClickListener;
    }
}