package br.com.damasceno.agenda.service.repository.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import br.com.damasceno.agenda.service.repository.database.dao.TaskDAO;
import br.com.damasceno.agenda.service.repository.database.dao.UserDAO;
import br.com.damasceno.agenda.service.model.Task;
import br.com.damasceno.agenda.service.model.User;

@Database(entities = {User.class, Task.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "app-database";
    private static volatile AppDatabase INSTANCE;

    public abstract UserDAO userDAO();
    public abstract TaskDAO taskDAO();

    public static synchronized AppDatabase getInstance(Context context) {

        if(INSTANCE == null) {

            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class, DB_NAME).build();

        }

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
