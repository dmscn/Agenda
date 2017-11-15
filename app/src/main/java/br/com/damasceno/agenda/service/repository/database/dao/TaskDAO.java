package br.com.damasceno.agenda.service.repository.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.com.damasceno.agenda.service.model.Task;

@Dao
public interface TaskDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertTask(Task task);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertTasks(List<Task> tasks);

    @Query("SELECT * FROM task")
    public LiveData<List<Task>> getAllTasks();

    @Delete
    public void removeTask(Task task);

    @Update
    public void updateTask(Task task);

}
