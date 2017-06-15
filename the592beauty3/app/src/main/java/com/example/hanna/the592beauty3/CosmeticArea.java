package com.example.hanna.the592beauty3;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by XNOTE on 2017-06-04.
 */

public class CosmeticArea {
    private static CosmeticArea mInstance;
    private RequestQueue requestQueue;
    private static Context mCtx;

    private CosmeticArea(Context context){
        mCtx = context;
        requestQueue = getRequestQueue();
    }
    public RequestQueue getRequestQueue(){
        if(requestQueue==null){
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return  requestQueue;
    }

    public static synchronized CosmeticArea getmInstance(Context context){
        if(mInstance==null){
            mInstance = new CosmeticArea(context);
        }
        return mInstance;
    }

    public <T> void addToRequestQue(Request<T> request){
        requestQueue.add(request);
    }
}
