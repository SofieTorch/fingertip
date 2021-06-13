package com.picateclas.fingertip;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.picateclas.fingertip.Interfaces.FirebaseListener;
import com.picateclas.fingertip.Interfaces.HttpSensorObserver;
import com.picateclas.fingertip.Models.DataSensor;
import com.picateclas.fingertip.Models.Member;
import com.picateclas.fingertip.Services.FirebaseService;
import com.picateclas.fingertip.Services.HttpSensorService;

import java.util.ArrayList;
import java.util.List;

public class activity_menu extends AppCompatActivity implements HttpSensorObserver, FirebaseListener {

    private String _memberId;
    private String _memberName;
    public TextView tvSubtitle, tvOxigenTitle, tvHearRateTitle;
    public LinearLayout lyOxigenation, lyHeartRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        tvSubtitle = findViewById(R.id.tvSubtitle);
        lyOxigenation = findViewById(R.id.lyOxigenation);
        lyHeartRate = findViewById(R.id.lyHeartRate);
        tvOxigenTitle = findViewById(R.id.tvOxigenTitle);
        tvHearRateTitle = findViewById(R.id.tvHeartRateTitle);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            _memberId = extras.getString("memberId");
            _memberName = extras.getString("memberName");
            tvSubtitle.setText("Registro del historial de: " + _memberName);
        }

        FirebaseService.getInstance().suscribe(this);
        FirebaseService.getInstance().getSensorDbData(this, _memberId);

        HttpSensorService.getInstance().setContext(this);
        HttpSensorService.getInstance().suscribe(this);
    }

    public void goBackHome(View view) {
        this.finish();
    }

    public void makeRequest(View view) {
        HttpSensorService.getInstance().getDatosServidor();
    }

    @Override
    public void onSensorDataReceived(ArrayList<DataSensor> datosSensor) {

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
        FirebaseService.getInstance().getSensorDbData(this, _memberId);
    }


    @Override
    public void onSensorDataFromDbReceived(List<DataSensor> data)
    {
        lyOxigenation.removeAllViews();
        lyHeartRate.removeAllViews();

        lyOxigenation.addView(tvOxigenTitle);
        lyHeartRate.addView(tvHearRateTitle);

        for (DataSensor dataSensor : data) {
            TextView tvSpo2 = new TextView(this);
            TextView tvHr = new TextView(this);

            tvSpo2.setText(dataSensor.getSpo2());
            tvHr.setText(dataSensor.getHr());

            lyOxigenation.addView(tvSpo2);
            lyHeartRate.addView(tvHr);
        }
    }

    @Override
    public void onIdVerificationCompleted(boolean idExists, String memberId) { }

    @Override
    public void onMembersReceived(List<Member> members) { }
}