package br.com.damasceno.agenda.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.Nullable;

import java.util.List;

import br.com.damasceno.agenda.service.model.Task;
import br.com.damasceno.agenda.service.repository.TaskRepository;
import br.com.damasceno.agenda.service.repository.webservice.VolleyResponseListener;

public class TaskViewModel extends AndroidViewModel {

    private TaskRepository mTaskRepository;
    private LiveData<List<Task>> mTasksObservable;

    public TaskViewModel(Application application) {
        super(application);

        mTaskRepository = TaskRepository.getInstance(getApplication());
        mTasksObservable = mTaskRepository.getTasks();

    }

    public LiveData<List<Task>> getTasksObservable() {
        return mTasksObservable;
    }

    public void refresh() {
        mTaskRepository.updateDatabase();
    }

    public void deleteTask(Task task) {

        mTaskRepository.removeTask(task, new VolleyResponseListener<Boolean>() {
            @Override
            public void onResponse(@Nullable Boolean response) {

            }

            @Override
            public void onError(@Nullable String error) {

            }
        });
    }

    public void createTask(Task task) {

        mTaskRepository.createTask(task, new VolleyResponseListener<Boolean>() {
            @Override
            public void onResponse(@Nullable Boolean response) {

            }

            @Override
            public void onError(@Nullable String error) {

            }
        });
    }

    public void updateTask(Task task) {

        mTaskRepository.updateTask(task, new VolleyResponseListener<Boolean>() {
            @Override
            public void onResponse(@Nullable Boolean response) {

            }

            @Override
            public void onError(@Nullable String error) {

            }
        });
    }

}
