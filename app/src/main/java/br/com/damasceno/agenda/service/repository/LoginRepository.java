package br.com.damasceno.agenda.service.repository;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.com.damasceno.agenda.constant.Constants;
import br.com.damasceno.agenda.service.model.User;
import br.com.damasceno.agenda.service.repository.database.AppDatabase;
import br.com.damasceno.agenda.service.repository.webservice.VolleySingleton;
import br.com.damasceno.agenda.util.SharedPreferencesUtils;

public class LoginRepository implements Constants {

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

    // Attempt Login
    public void login(String credentialsToken, Response.Listener<Integer> listener) {

        // Request URL to login
        String url = URL_BASE + URL_AUTH;

        // Data Params to JSON
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("access_token", WEB_SERVER_MASTER_KEY);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                (JSONObject response) -> {

                    try {

                        final User profile;

                        // JSON String User
                        String jsonUser = response.getJSONObject("user").toString();

                        // Mapping the JSON and filling the User
                        ObjectMapper mapper = new ObjectMapper();
                        profile = mapper.readValue(jsonUser, User.class);

                        // Save User Token
                        profile.setToken(response.get("token").toString());

                        // Storing User Profile
                        SharedPreferencesUtils.storeUserProfile(mContext.getApplicationContext(), credentialsToken, profile.getToken(), profile.getName(), profile.getEmail(), profile.getPicture());

                        // Save User in Database
                        AsyncTask.execute(() -> {

                            AppDatabase db = AppDatabase.getInstance(mContext);
                            db.userDAO().insertUser(profile);

                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    listener.onResponse(201);
                },
                error -> {

                    // Log Volley Error
                    Log.i(TAG_LOG, "Volley Error: " + error.toString());

                    // Get the Network Response
                    NetworkResponse networkResponse = error.networkResponse;

                    Integer statusCode = null;

                    // Verify if there is a status code
                    if(networkResponse != null) {
                        statusCode = networkResponse.statusCode;
                    }

                    // Return Status Code
                    listener.onResponse(statusCode);
                }) {

            // Setting up a new Header
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String,String> params = new HashMap<String, String>();
                params.put("Authorization", credentialsToken);

                return params;
            }
        };

        jsonRequest.setTag(TAG_REQUEST_LOGIN);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonRequest);
    }


    // Attempt Register
    public void register(User user, final Response.Listener<Integer> listener) {

        // Request URL
        String url = URL_BASE + URL_USER;

        // Data Params to JSON
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("access_token", WEB_SERVER_MASTER_KEY);
        params.put("email", user.getEmail());
        params.put("password", user.getPassword());
        params.put("name", user.getName());

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                response -> {

                    try {

                        final User profile;

                        // JSON String User
                        String jsonUser = response.toString();

                        // Mapping the String and filling User object
                        ObjectMapper mapper = new ObjectMapper();
                        profile = mapper.readValue(jsonUser, User.class);

                        // Generating credentials token
                        String credentialsToken = "Basic " + Base64.encodeToString((profile.getEmail() + ":" +profile.getPassword()).getBytes(), Base64.DEFAULT);

                        // Storing User Profile
                        SharedPreferencesUtils.storeUserProfile(mContext, credentialsToken, profile.getToken() ,profile.getName(), profile.getEmail(), profile.getPicture());

                        // Save User in Database
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {

                                AppDatabase db = AppDatabase.getInstance(mContext);
                                db.userDAO().insertUser(profile);
                            }
                        });

                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                    listener.onResponse(201);
                },
                error -> {

                    // Log Volley Error
                    Log.i(TAG_LOG, "Volley Error: " + error.toString());

                    // Get the Network Response
                    NetworkResponse networkResponse = error.networkResponse;

                    Integer statusCode = null;

                    // Verify if there is a status code
                    if(networkResponse != null) {
                        statusCode = networkResponse.statusCode;
                    }

                    // Return Status Code
                    listener.onResponse(statusCode);
                });

        jsonRequest.setTag(TAG_REQUEST_REGISTER);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonRequest);

    }
}
