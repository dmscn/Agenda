package br.com.damasceno.agenda.fragment;


import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import br.com.damasceno.agenda.activity.R;
import br.com.damasceno.agenda.adapter.TaskAdapter;
import br.com.damasceno.agenda.constant.Constants;
import br.com.damasceno.agenda.database.AppDatabase;
import br.com.damasceno.agenda.helper.RecyclerItemTouchHelper;
import br.com.damasceno.agenda.helper.RecyclerItemTouchHelper.RecyclerItemTouchHelperListener;
import br.com.damasceno.agenda.helper.VolleyResponseListener;
import br.com.damasceno.agenda.model.Task;
import br.com.damasceno.agenda.util.SharedPreferencesUtils;
import br.com.damasceno.agenda.util.ToastUtils;
import br.com.damasceno.agenda.util.VolleyUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskFragment extends Fragment implements RecyclerItemTouchHelperListener, Constants {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.main_layout)
    FrameLayout mMainLayout;

    private TaskAdapter mAdapter;
    private List<Task> mTasks = new ArrayList<Task>();
    private LinearLayoutManager mLayoutManager;

    public TaskFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        ButterKnife.bind(this, view);

        // Setting up LayoutManager for RecyclerView Adapter
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        // Setting up RecyclerView
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // RecyclerView Adapter
        mAdapter = new TaskAdapter(mTasks, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        // Adding ItemTouchHelper
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);

        // Update Data
        mAdapter.fetchData();

        return view;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if(viewHolder instanceof TaskAdapter.ViewHolder) {

            // get the removed item name to display it in snack bar
            String name = mTasks.get(viewHolder.getAdapterPosition()).getTitle();

            // backup of removed task for undo purpose
            final Task deletedTask = mTasks.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the task from recycler view
            mAdapter.removeTask(deletedIndex);

            // showing snack bar with undo option
            Snackbar snackbar = Snackbar
                    .make(mMainLayout, name + " " + getString(R.string.msg_task_accomplished), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.str_undo), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            // undo is selected, restore the deleted task
                            mAdapter.restoreTask(deletedTask, deletedIndex);
                        }
                    })
                    .setActionTextColor(getResources().getColor(R.color.colorAccent))
                    .addCallback(new Snackbar.Callback() {

                        // Officially remove the task after snackbar is dismissed
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            super.onDismissed(transientBottomBar, event);

                            switch (event) {

                                // Only removes after timeout and user didn't clicked UNDO
                                case Snackbar.Callback.DISMISS_EVENT_TIMEOUT:

                                    // Remove from the database
                                    AsyncTask.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            AppDatabase.getInstance(getActivity()).taskDAO().removeTask(deletedTask);
                                        }
                                    });

                                    // Getting User Access Token
                                    String userAccessToken = SharedPreferencesUtils.getUserAccessToken(getActivity());

                                    // Remove from the Server
                                    VolleyUtils.deleteTask(getActivity(), userAccessToken, deletedTask.getId(), new VolleyResponseListener<Boolean>() {

                                        @Override
                                        public void onResponse(@Nullable Boolean response) {

                                        }

                                        @Override
                                        public void onError(@Nullable String error) {

                                            ToastUtils.toast(getActivity(), getString(R.string.msg_error_removing_task));
                                        }

                                    });
                                    break;
                            }
                        }
                    });

            snackbar.show();
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
