package br.com.damasceno.agenda.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.Image;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.ArrayList;
import java.util.List;

import br.com.damasceno.agenda.activity.R;
import br.com.damasceno.agenda.model.Task;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Task> tasks;
    private Context context;

    public TaskAdapter(List<Task> tasks, Context context) {
        this.tasks = tasks;
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

        Task task = tasks.get(position);

        holder.title.setText(task.getTitle());
        holder.text.setText(task.getText());
        holder.label.setText(task.getLabel());

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void removeTask(int position) {
        tasks.remove(position);

        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: dont call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreTask(Task task, int position) {
        tasks.add(position, task);

        // notify item added by position
        notifyItemInserted(position);
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
