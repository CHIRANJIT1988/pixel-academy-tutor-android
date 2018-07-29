package pixel.academy.tutor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pixel.academy.tutor.R;
import pixel.academy.tutor.model.Education;


public class PingRecyclerAdapter extends RecyclerView.Adapter<PingRecyclerAdapter.VersionViewHolder>
{

    private List<Education> educationList;

    private Context context;
    private OnItemClickListener clickListener;


    public PingRecyclerAdapter(Context context, List<Education> educationList)
    {
        this.context = context;
        this.educationList = educationList;
    }


    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerlist_item_ping, viewGroup, false);
        return new VersionViewHolder(view);
    }


    @Override
    public void onBindViewHolder(VersionViewHolder versionViewHolder, int i)
    {

        /*Education education = educationList.get(i);

        versionViewHolder.school_name.setText(education.college.toUpperCase());
        versionViewHolder.qualification.setText(education.qualification.toUpperCase() + " - " + education.division);
        versionViewHolder.achievement.setText(education.academic_achievement);
        versionViewHolder.qualification_status.setText(education.qualification_status.toUpperCase());

        versionViewHolder.ibEdit.setTag(i);*/
    }


    @Override
    public int getItemCount()
    {
        return educationList == null ? 0 : educationList.size();
    }


    class VersionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        //TextView school_name;
        //TextView qualification;
        //TextView achievement;
        //TextView qualification_status;
        //ImageButton ibEdit;


        VersionViewHolder(View itemView)
        {

            super(itemView);

            /*school_name = (TextView) itemView.findViewById(R.id.school_name);
            qualification = (TextView) itemView.findViewById(R.id.qualification);
            achievement = (TextView) itemView.findViewById(R.id.achievement);
            qualification_status = (TextView) itemView.findViewById(R.id.status);
            ibEdit = (ImageButton) itemView.findViewById(R.id.ibEdit);

            ibEdit.setOnClickListener(onButtonClickListener);*/
            itemView.setOnClickListener(this);
        }


        /*private View.OnClickListener onButtonClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {

                if (v.getId() == R.id.ibEdit)
                {
                    Intent intent = new Intent(context, EditProfileActivity.class);
                    intent.putExtra("FRAGMENT", "EDUCATION");
                    intent.putExtra("EDIT", true);
                    intent.putExtra("EDUCATION_OBJ", educationList.get((int) v.getTag()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        };*/


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