package br.com.damasceno.agenda.service.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import br.com.damasceno.agenda.constant.Constants;
import br.com.damasceno.agenda.service.model.Task;
import br.com.damasceno.agenda.service.repository.database.AppDatabase;
import br.com.damasceno.agenda.service.repository.webservice.VolleyResponseListener;
import br.com.damasceno.agenda.service.repository.webservice.VolleySingleton;
import br.com.damasceno.agenda.util.SharedPreferencesUtils;

public class TaskRepository implements Constants {

    private static volatile TaskRepository INSTANCE;

    private static String mUserAccessToken;
    private Context mContext;
    private AppDatabase mDatabase;

    private LiveData<List<Task>> mTaskList;



    private TaskRepository(Context context, String userAccessToken) {

        mContext = context.getApplicationContext();
        mUserAccessToken = userAccessToken;
        mDatabase = AppDatabase.getInstance(context);

    }

    public static synchronized TaskRepository getInstance(Context context) {

        if(INSTANCE == null) {

            String userAccessToken = SharedPreferencesUtils.getUserAccessToken(context);

            INSTANCE = new TaskRepository(context, userAccessToken);
        }

        return INSTANCE;

    }

    // If is the first time, update database with WebServer data before returning the list
    public LiveData<List<Task>> getTasks() {

        if(mTaskList == null) {

            updateDatabase();
            mTaskList = mDatabase.taskDAO().getAllTasks();

        }

        return mTaskList;
    }

    // Update database with WebServer data
    public void updateDatabase() {

        // Request URL
        String url = URL_BASE + URL_TASK + "?access_token=" + mUserAccessToken;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {

                    Log.i(TAG_LOG, "RESPONSE -> " + response);

                    final List<Task> taskList;

                    try {
                        // Mapping JSON and filling list
                        ObjectMapper mapper = new ObjectMapper();
                        taskList = mapper.readValue(response, new TypeReference<List<Task>>(){});

                        // Upadate the Database
                        AsyncTask.execute(() -> mDatabase.taskDAO().insertTasks(taskList));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

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
                });

        stringRequest.setTag(TAG_REQUEST_ALL_TASKS);
        VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
    }

    // Create new task
    public void createTask(Task task, final VolleyResponseListener<Boolean> listener) {

        // Request URL
        String url = URL_BASE + URL_TASK;

        // Data Params to JSON
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("access_token", mUserAccessToken);
        params.put("title", task.getTitle());
        params.put("label", task.getLabel());
        params.put("text", task.getText());

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                response -> {

                    try {

                        Task taskResponse;

                        // JSON String User
                        String jsonUser = response.toString();

                        // Mapping the String and filling User object
                        ObjectMapper mapper = new ObjectMapper();
                        taskResponse = mapper.readValue(jsonUser, Task.class);

                        // Inserting in database
                        AsyncTask.execute(() -> mDatabase.taskDAO().insertTask(taskResponse));

                        listener.onResponse(true);

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                },
                error -> {

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
                });

        jsonRequest.setTag(TAG_REQUEST_ADD_NEW_TASK);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonRequest);
    }

    // Remove Task from WebServer
    public void removeTask(Task task, final VolleyResponseListener<Boolean> listener) {

        // Remove Task from Database
        AsyncTask.execute(() -> mDatabase.taskDAO().removeTask(task));

        String taskId = task.getId();

        // Request URL
        String url = URL_BASE + URL_TASK + "/" + taskId + "?access_token=" + mUserAccessToken;

        // Request
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                response -> {
                    Log.i(TAG_LOG, "Task successfully removed from WebServer");
                    listener.onResponse(true);
                },
                error -> {
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
                });

        stringRequest.setTag(TAG_REQUEST_REMOVE_TASK);
        VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
    }

    public void updateTask(Task task, final VolleyResponseListener<Boolean> listener) {

        // Remove Task from Database
        AsyncTask.execute(() -> mDatabase.taskDAO().updateTask(task));

        String taskId = task.getId();

        // Request URL
        String url = URL_BASE + URL_TASK + "/" + taskId + "?access_token=" + mUserAccessToken;

        // Data Params to JSON
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("access_token", mUserAccessToken);
        params.put("title", task.getTitle());
        params.put("label", task.getLabel());
        params.put("text", task.getText());

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.PUT, url, new JSONObject(params),
                response -> {

                    try {

                        Task taskResponse;

                        // JSON String User
                        String jsonUser = response.toString();

                        // Mapping the String and filling User object
                        ObjectMapper mapper = new ObjectMapper();
                        taskResponse = mapper.readValue(jsonUser, Task.class);

                        listener.onResponse(true);

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                },
                error -> {

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
                });

        jsonRequest.setTag(TAG_REQUEST_UPDATE_TASK);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonRequest);

    }
}
