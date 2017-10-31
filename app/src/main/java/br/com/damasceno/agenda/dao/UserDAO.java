package br.com.damasceno.agenda.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import br.com.damasceno.agenda.model.User;

@Dao
public interface UserDAO {

    @Insert
    public void insertUser(User user);

    @Query("SELECT * FROM user LIMIT 1")
    public User getProfile();

    @Delete
    public void removeProfile(User user);

}
