package br.com.damasceno.agenda.service.repository.webservice;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.com.damasceno.agenda.constant.Constants;
import br.com.damasceno.agenda.service.model.User;
import br.com.damasceno.agenda.service.repository.database.AppDatabase;
import br.com.damasceno.agenda.util.SharedPreferencesUtils;

public class VolleyUtils implements Constants {

    private static final String WEB_SERVER_MASTER_KEY = "QYiVImowlxDeI8rWwBf7pCZ2j3g72gxk";
    private static final String URL_BASE = "https://myrestfulapi.herokuapp.com";
    private static final String URL_AUTH = "/auth";
    private static final String URL_USER = "/users";
    private static final String URL_TASK = "/tasks";

    private static Context mContext;
    private static volatile VolleyUtils INSTANCE;

    private VolleyUtils(Context context){
        mContext = context;
    }

    public static synchronized VolleyUtils getInstance(Context context) {

        if(INSTANCE == null) {

            INSTANCE = new VolleyUtils(context);

        }

        return INSTANCE;

    }

    // TODO make it SYNCHRONOUS
    // Attempt Login
    public static void requestAuth(final Context context, final String credentialsToken, final VolleyResponseListener listener) {

        // Request URL to login
        String url = URL_BASE + URL_AUTH;

        // Data Params to JSON
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("access_token", WEB_SERVER_MASTER_KEY);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

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
                            SharedPreferencesUtils.storeUserProfile(context.getApplicationContext(), credentialsToken, profile.getToken(), profile.getName(), profile.getEmail(), profile.getPicture());

                            // Save User in Database
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {

                                    AppDatabase db = AppDatabase.getInstance(context);
                                    db.userDAO().insertUser(profile);
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // Log Volley Error
                        Log.i(TAG_LOG, "Volley Error: " + error.toString());

                        // Get the Network Response
                        NetworkResponse networkResponse = error.networkResponse;

                        String statusCode = null;

                        // Verify if there is a status code
                        if(networkResponse != null) {
                            statusCode = String.valueOf(networkResponse.statusCode);
                        }

                        // Return Status Code
                        listener.onError(statusCode);
                    }
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
        VolleySingleton.getInstance(context).addToRequestQueue(jsonRequest);
    }

    // TODO make it SYNCHRONOUS
    // Attempt Resgister
    public static void requestRegister(final Context context, User user, final VolleyResponseListener listener) {

        // Request URL
        String url = URL_BASE + URL_USER;

        // Data Params to JSON
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("access_token", WEB_SERVER_MASTER_KEY);
        params.put("email", user.getEmail());
        params.put("password", user.getPassword());
        params.put("name", user.getName());

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

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
                            SharedPreferencesUtils.storeUserProfile(context.getApplicationContext(), credentialsToken, profile.getToken() ,profile.getName(), profile.getEmail(), profile.getPicture());

                            // Save User in Database
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {

                                    AppDatabase db = AppDatabase.getInstance(context);
                                    db.userDAO().insertUser(profile);
                                }
                            });

                        } catch (Exception e) {

                            e.printStackTrace();
                        }

                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // Log Volley Error
                        Log.i(TAG_LOG, "Volley Error: " + error.toString());

                        // Get the Network Response
                        NetworkResponse networkResponse = error.networkResponse;

                        String statusCode = null;

                        // Verify if there is a status code
                        if(networkResponse != null) {
                            statusCode = String.valueOf(networkResponse.statusCode);
                        }

                        // Return Status Code
                        listener.onError(statusCode);
                    }
                });

        jsonRequest.setTag(TAG_REQUEST_REGISTER);
        VolleySingleton.getInstance(context).addToRequestQueue(jsonRequest);

    }

}
