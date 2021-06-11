package com.picateclas.fingertip.Services;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.picateclas.fingertip.Interfaces.HttpSensorObserver;
import com.picateclas.fingertip.Models.DataSensor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HttpSensorService {
    private static HttpSensorService instance;

    ArrayList<DataSensor> listDate = new ArrayList<DataSensor>();

    private static ArrayList<HttpSensorObserver> suscribers;

    private Context context;


    private HttpSensorService()
    {
        suscribers = new ArrayList<HttpSensorObserver>();
    }

    public static HttpSensorService getInstance()
    {
        if (instance == null)
        {
            instance = new HttpSensorService();
        }
        return instance;
    }

    public void setContext(Context context)
    {
        this.context = context;
    }

    public void suscribe(HttpSensorObserver suscriber)
    {
        suscribers.add(suscriber);
    }




    public   void getDatosServidor()
    {
        listDate = new ArrayList<DataSensor>();
        RequestQueue queue = Volley.newRequestQueue(context);
        String URL ="https://47689b84f889.ngrok.io/sensor";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i = 0; i < 6; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                        String hr = jsonObject.getString("HRvalid");
                        String spo2 = jsonObject.getString("validSPO2");
                        listDate.add(new DataSensor(hr, spo2));
                    }
                    for (HttpSensorObserver suscriber : suscribers) {
                        suscriber.onSensorDataReceived(listDate);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("HttpSensorService", "NO FUNCIONA PE");
            }
        });

        queue.add(stringRequest);

    }
}
