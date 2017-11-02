package br.com.damasceno.agenda.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import br.com.damasceno.agenda.activity.R;
import br.com.damasceno.agenda.helper.VolleyResponseListener;
import br.com.damasceno.agenda.model.Task;
import br.com.damasceno.agenda.util.SharedPreferencesUtils;
import br.com.damasceno.agenda.util.VolleyUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Task> mTaskList;
    private Context context;

    public TaskAdapter(List<Task> tasks, Context context) {
        this.mTaskList = tasks;
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

        Task task = mTaskList.get(position);

        holder.title.setText(task.getTitle());
        holder.text.setText(task.getText());
        holder.label.setText(task.getLabel());

    }

    @Override
    public int getItemCount() {
        return mTaskList.size();
    }

    public void fetchData() {

        // Gettin User Credentials to Fetch his personal data from the server
        String userAccessToken = SharedPreferencesUtils.getUserAccessToken(context);

        // Requesting Data from the Server
        VolleyUtils.requestAllTasks(context, userAccessToken, new VolleyResponseListener<List<Task>>() {

            @Override
            public void onResponse(@Nullable List<Task> response) {

                // Update Data in RecyclerAdapter
                mTaskList.clear();
                mTaskList.addAll(response);
                notifyDataSetChanged();
            }

            @Override
            public void onError(@Nullable String error) {

            }
        });
    }

    public void addTask(Task task) {

        mTaskList.add(task);

        // TODO: save in database
    }

    public void removeTask(int position) {

        mTaskList.remove(position);

        // TODO: remove in database

        // notify the item removed by position
        notifyItemRemoved(position);
    }

    public void restoreTask(Task task, int position) {

        mTaskList.add(position, task);

        // TODO: save in database

        // notify item added by position
        notifyItemInserted(position);
    }

    public void updateServer() {

        // TODO: call Volley method to send new list of tasks
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        public TextView title;

        @BindView(R.id.text)
        public TextView text;

        @BindView(R.id.label)
        public TextView label;

        @BindView(R.id.view_foreground)
        public RelativeLayout viewForeground;

        @BindView(R.id.view_background)
        public RelativeLayout viewBackground;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
