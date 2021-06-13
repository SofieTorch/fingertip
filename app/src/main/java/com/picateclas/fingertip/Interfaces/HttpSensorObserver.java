package com.picateclas.fingertip.Interfaces;

import com.picateclas.fingertip.Models.DataSensor;

import java.util.ArrayList;

public interface HttpSensorObserver {
    void onSensorDataReceived(ArrayList<DataSensor> datosSensor);
}
