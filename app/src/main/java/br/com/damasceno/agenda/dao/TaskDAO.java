package br.com.damasceno.agenda.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import br.com.damasceno.agenda.model.Task;

@Dao
public interface TaskDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertTask(Task task);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertTasks(List<Task> tasks);

    @Query("SELECT * FROM task")
    public List<Task> loadAllTasks();

    @Delete
    public void removeTask(Task task);

}
