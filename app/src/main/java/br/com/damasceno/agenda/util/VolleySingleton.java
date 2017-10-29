package br.com.damasceno.agenda.util;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {

    private static VolleySingleton mInstance;
    private RequestQueue mRequestQueue;
    private static Context mContext;

    private VolleySingleton(Context context) {

        // Specify the application context
        mContext = context;

        // Get the RequestQueue;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleySingleton getInstance(Context context) {

        // If instance is null then initialize new Instance
        if(mInstance == null) {
            mInstance = new VolleySingleton(context);
        }

        return mInstance;
    }

    public RequestQueue getRequestQueue() {

        // If RequestQueue is null then initialize new RequestQueue
        if(mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }

        return mRequestQueue;
    }

    public<T> void addToRequestQueue(Request<T> request) {
        // Add the specified request to the RequestQueue
        getRequestQueue().add(request);
    }

}
