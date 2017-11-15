package br.com.damasceno.agenda.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.damasceno.agenda.view.ui.activity.TaskActivity;
import br.com.damasceno.agenda.view.ui.activity.R;
import br.com.damasceno.agenda.service.model.Task;
import butterknife.BindView;
import butterknife.ButterKnife;

import static br.com.damasceno.agenda.constant.Constants.PARAM_TASK;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Task> mTaskList = new ArrayList<Task>();
    private Context context;

    public TaskAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Task task = mTaskList.get(position);

        holder.title.setText(task.getTitle());
        holder.text.setText(task.getText());
        holder.label.setText(task.getLabel());

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Start Activity to show the Task
                Intent intent = new Intent(context, TaskActivity.class);
                intent.putExtra(PARAM_TASK, task);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mTaskList.size();
    }


    public void addTask(Task task) {

        mTaskList.add(task);
    }

    public void updateDataSet(List<Task> updatedTaskList) {

        mTaskList.clear();
        mTaskList.addAll(updatedTaskList);
        notifyDataSetChanged();
    }

    public void removeTask(final int position) {

        mTaskList.remove(position);

        // notify the item removed by position
        notifyItemRemoved(position);
    }

    public void restoreTask(final Task task, int position) {

        mTaskList.add(position, task);

        // notify item added by position
        notifyItemInserted(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        public TextView title;

        @BindView(R.id.text)
        public TextView text;

        @BindView(R.id.lay_label)
        public TextView label;

        @BindView(R.id.view_foreground)
        public RelativeLayout viewForeground;

        @BindView(R.id.view_background)
        public RelativeLayout viewBackground;

        @BindView(R.id.main_layout)
        FrameLayout mainLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
