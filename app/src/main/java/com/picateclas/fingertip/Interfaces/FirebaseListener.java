package com.picateclas.fingertip.Interfaces;

import com.picateclas.fingertip.Models.DataSensor;
import com.picateclas.fingertip.Models.Member;

import java.util.List;

public interface FirebaseListener {
    void onMembersReceived(List<Member> members);
    void onSensorDataFromDbReceived(List<DataSensor> data);
    void onIdVerificationCompleted(boolean idExists, String memberId);
}
