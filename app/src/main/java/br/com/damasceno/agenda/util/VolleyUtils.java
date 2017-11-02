package br.com.damasceno.agenda.util;

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
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.damasceno.agenda.activity.R;
import br.com.damasceno.agenda.constant.Constants;
import br.com.damasceno.agenda.database.AppDatabase;
import br.com.damasceno.agenda.helper.VolleyResponseListener;
import br.com.damasceno.agenda.model.Task;
import br.com.damasceno.agenda.model.User;

public class VolleyUtils implements Constants {

    public static final String WEB_SERVER_MASTER_KEY = "QYiVImowlxDeI8rWwBf7pCZ2j3g72gxk";
    public static final String URL_BASE = "https://myrestfulapi.herokuapp.com";
    public static final String URL_AUTH = "/auth";
    public static final String URL_USER = "/users";
    public static final String URL_TASK = "/tasks";

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

    public static void requestAllTasks(final Context context, String userAccessToken, final VolleyResponseListener<List<Task>> listener) {

        // Request URL
        String url = URL_BASE + URL_TASK + "?access_token=" + userAccessToken;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i(TAG_LOG, "RESPONSE -> " + response);

                        final List<Task> taskList;

                        try {
                            // Mapping JSON and filling list
                            ObjectMapper mapper = new ObjectMapper();
                            taskList = mapper.readValue(response, new TypeReference<List<Task>>(){});

                            // Updating DataBase
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {

                                    AppDatabase db = AppDatabase.getInstance(context);
                                    db.taskDAO().insertTasks(taskList);
                                }
                            });

                            // Return the Task list
                            listener.onResponse(taskList);

                        } catch (IOException e) {

                            e.printStackTrace();
                            listener.onError(context.getString(R.string.msg_error_could_not_fetch_from_server));
                        }
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

                        Log.i(TAG_LOG, "Status :" + statusCode);

                        // Return Status Code
                        listener.onError(statusCode);
                    }
                });

        stringRequest.setTag(TAG_REQUEST_ALL_TASKS);
        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }

}
