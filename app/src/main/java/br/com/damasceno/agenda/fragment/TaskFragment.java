package br.com.damasceno.agenda.fragment;


import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.com.damasceno.agenda.activity.R;
import br.com.damasceno.agenda.adapter.TaskAdapter;
import br.com.damasceno.agenda.helper.RecyclerItemTouchHelper;
import br.com.damasceno.agenda.helper.RecyclerItemTouchHelper.RecyclerItemTouchHelperListener;
import br.com.damasceno.agenda.model.Task;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskFragment extends Fragment implements RecyclerItemTouchHelperListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private TaskAdapter adapter;
    private List<Task> tasks;

    @BindView(R.id.main_layout)
    FrameLayout mainLayout;

    public TaskFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        ButterKnife.bind(this, view);

        tasks = new ArrayList<>();

        for(int i=0; i<10; i++) {
            Task task = new Task();

            task.setTitle("Title " + (i+1));
            task.setLabel("Label" + (i+1));
            task.setText("This is a dummy text");

            tasks.add(task);
        }

        adapter = new TaskAdapter(tasks, getActivity());

        // setting up recycler view
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        // adding item touch helper
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        return view;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof TaskAdapter.ViewHolder) {

            // get the removed item name to display it in snack bar
            String name = tasks.get(viewHolder.getAdapterPosition()).getTitle();

            // backup of removed task for undo purpose
            final Task deletedTask = tasks.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the task from recycler view
            adapter.removeTask(viewHolder.getAdapterPosition());

            // showing snack bar with undo option
            Snackbar snackbar = Snackbar
                    .make(mainLayout, name + " " + getString(R.string.msg_task_accomplished), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.str_undo), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            // undo is selected, restore the deleted task
                            adapter.restoreTask(deletedTask, deletedIndex);
                        }
                    })
                    .setActionTextColor(Color.YELLOW);

            snackbar.show();
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
