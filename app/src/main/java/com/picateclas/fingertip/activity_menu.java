package com.picateclas.fingertip;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.picateclas.fingertip.Interfaces.HttpSensorObserver;
import com.picateclas.fingertip.Models.DataSensor;
import com.picateclas.fingertip.Services.FirebaseService;
import com.picateclas.fingertip.Services.HttpSensorService;

import java.util.ArrayList;

public class activity_menu extends AppCompatActivity implements HttpSensorObserver {

    private String _memberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            _memberId = extras.getString("memberId");
        }

        HttpSensorService.getInstance().setContext(this);
        HttpSensorService.getInstance().suscribe(this);
    }

    public void makeRequest(View view) {
        HttpSensorService.getInstance().getDatosServidor();
    }

    @Override
    public void onSensorDataReceived(ArrayList<DataSensor> datosSensor) {
        // textView.setText("");


        double sumaOxigeno = 0;
        double sumaPulso = 0;

        for (DataSensor item: datosSensor )
        {
            sumaPulso += Double.parseDouble(item.getHr());
            sumaOxigeno += Double.parseDouble(item.getSpo2());
        }
        double promedioOxigeno = sumaOxigeno/5;
        double promedioPulso = sumaPulso/5;

        FirebaseService.getInstance().sendSensorData(this, _memberId, promedioOxigeno, promedioPulso);

        // textView.setText("Oxigeno " + promedioOxigenoString +  " Pulso " +promedioPulsoString);
    }
}