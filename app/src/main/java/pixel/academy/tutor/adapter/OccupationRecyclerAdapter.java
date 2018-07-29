package pixel.academy.tutor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import pixel.academy.tutor.R;
import pixel.academy.tutor.helper.Helper;
import pixel.academy.tutor.helper.OnTaskCompleted;
import pixel.academy.tutor.model.Occupation;


public class OccupationRecyclerAdapter extends RecyclerView.Adapter<OccupationRecyclerAdapter.VersionViewHolder>
{

    private Context context;
    private OnItemClickListener clickListener;
    private OnTaskCompleted listener;


    public OccupationRecyclerAdapter(Context context, OnTaskCompleted listener)
    {
        this.context = context;
        this.listener = listener;
    }


    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerlist_item_occupations, viewGroup, false);
        return new VersionViewHolder(view);
    }


    @Override
    public void onBindViewHolder(VersionViewHolder vHolder, int i)
    {
        vHolder.bindData(Occupation.occupationList.get(i));
        vHolder.btnDelete.setTag(i);
        vHolder.btnEdit.setTag(i);
    }


    @Override
    public int getItemCount()
    {
        return Occupation.occupationList == null ? 0 : Occupation.occupationList.size();
    }


    class VersionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView occupation;
        private TextView organization;
        private TextView date;
        private Button btnEdit;
        private Button btnDelete;

        VersionViewHolder(View itemView)
        {

            super(itemView);

            occupation = (TextView) itemView.findViewById(R.id.occupation);
            organization = (TextView) itemView.findViewById(R.id.organization);
            date = (TextView) itemView.findViewById(R.id.date);
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

        private void bindData(Occupation _occupation)
        {
            occupation.setText(Helper.toCamelCase(_occupation.occupation));
            organization.setText(Helper.toCamelCase(_occupation.organization));

            if(!_occupation.start_date.isEmpty())
            {
                if(!_occupation.end_date.isEmpty())
                {
                    date.setText(String.valueOf(_occupation.start_date + " - " + _occupation.end_date));
                }

                else
                {
                    date.setText(String.valueOf(_occupation.start_date + " - " + "NOW"));
                }
            }
        }

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