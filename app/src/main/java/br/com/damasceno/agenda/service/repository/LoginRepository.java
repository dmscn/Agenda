package br.com.damasceno.agenda.service.repository;

import android.content.Context;

import br.com.damasceno.agenda.service.repository.database.AppDatabase;
import br.com.damasceno.agenda.util.SharedPreferencesUtils;

public class LoginRepository {
    private static volatile LoginRepository INSTANCE;

    private static String mUserAccessToken;
    private Context mContext;
    private AppDatabase mDatabase;



    private LoginRepository(Context context, String userAccessToken) {

        mContext = context.getApplicationContext();
        mUserAccessToken = userAccessToken;
        mDatabase = AppDatabase.getInstance(context);

    }

    public static synchronized LoginRepository getInstance(Context context) {

        if(INSTANCE == null) {

            String userAccessToken = SharedPreferencesUtils.getUserAccessToken(context);

            INSTANCE = new LoginRepository(context, userAccessToken);
        }

        return INSTANCE;

    }
}
