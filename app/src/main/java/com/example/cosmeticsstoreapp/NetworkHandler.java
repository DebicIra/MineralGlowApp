package com.example.cosmeticsstoreapp;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class NetworkHandler {
    private static NetworkHandler instance;
    private RequestQueue requestQueue;
    private final Context context;

    private NetworkHandler(Context context) {
        this.context = context.getApplicationContext();
        requestQueue = getRequestQueue();
    }

    public static synchronized NetworkHandler getInstance(Context context) {
        if (instance == null) {
            instance = new NetworkHandler(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }
}
