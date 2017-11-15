package br.com.damasceno.agenda.service.repository.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import br.com.damasceno.agenda.service.model.User;

@Dao
public interface UserDAO {

    @Insert
    public void insertUser(User user);

    @Query("SELECT * FROM user LIMIT 1")
    public User getProfile();

    @Query("DELETE FROM user WHERE user.email = :email")
    public void removeUserByEmail(String email);

}
