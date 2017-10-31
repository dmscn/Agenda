package br.com.damasceno.agenda.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import br.com.damasceno.agenda.model.Task;

@Dao
public interface TaskDAO {

    @Insert
    public void insertTask(Task task);

    @Query("SELECT * FROM task")
    public Task[] loadAllTasks();

}
