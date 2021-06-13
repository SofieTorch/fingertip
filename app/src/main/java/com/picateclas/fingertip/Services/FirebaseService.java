package com.picateclas.fingertip.Services;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.picateclas.fingertip.Interfaces.FirebaseListener;
import com.picateclas.fingertip.Models.DataSensor;
import com.picateclas.fingertip.Models.Member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class FirebaseService {
    private final FirebaseDatabase _database;
    private static FirebaseService _instance;

    private Stack<FirebaseListener> _suscribers;

    private FirebaseService() {
        _suscribers = new Stack<FirebaseListener>();
        _database = FirebaseDatabase.getInstance();
    }

    public void suscribe(FirebaseListener listener) {
        if (_suscribers.size() == 2) {
            _suscribers.pop();
        }
        _suscribers.push(listener);
    }

    public static FirebaseService getInstance() {
        if (_instance == null) {
            _instance = new FirebaseService();
        }
        return _instance;
    }

    public String registerNewMember(Member member) {
        DatabaseReference membersDb = _database.getReference("members");
        String memberId = membersDb.push().getKey();
        membersDb.child(memberId).setValue(member);

        return memberId;
    }

    public void getMembersDataBase(Context context) {
        FileService fileService = new FileService(context);
        List<String> membersIds = fileService.getMembersIds();
        List<Member> members = new ArrayList<Member>();

        for (String memberId: membersIds) {
            DatabaseReference membersDb = _database.getReference("members");

            membersDb.child(memberId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {

                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        String name = String.valueOf(task.getResult().child("name").getValue());
                        String age = String.valueOf(task.getResult().child("age").getValue());
                        members.add(new Member(name, Integer.parseInt(age), memberId));

                        if (members.size() == membersIds.size()) {
                            for(FirebaseListener listener : _suscribers) {
                                listener.onMembersReceived(members);
                            }
                        }

                    }
                }
            });
        }
    }

    public void verifyIfIdExists(String id) {
        DatabaseReference members = _database.getReference("members");
        members.child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    if(task.getResult().getValue() == null) {
                        for(FirebaseListener listener : _suscribers) {
                            listener.onIdVerificationCompleted(false, id);
                        }
                    }
                    else {
                        for(FirebaseListener listener : _suscribers) {
                            listener.onIdVerificationCompleted(true, id);
                        }
                    }
                }
            }
        });
    }

    public void sendSensorData(Context context, String id, double avgOxigen, double avgHeartRate) {
        DatabaseReference memberSensorData = _database.getReference("members/" + id + "/HRSpO2Data");

        String dataId = memberSensorData.push().getKey();
        DataSensor data = new DataSensor(String.valueOf(avgHeartRate), String.valueOf(avgOxigen));
        memberSensorData.child(dataId).setValue(data);

    }

    public void getSensorDbData(Context context, String id) {
        List<DataSensor> listSensorData = new ArrayList<DataSensor>();
        DatabaseReference memberOxigen = _database.getReference("members/" + id + "/HRSpO2Data");

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("firebase", "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                // DataSensor sensorData = dataSnapshot.getValue(DataSensor.class);

                Map<String, Object>  data = (HashMap<String, Object>) dataSnapshot.getValue();
                String hr = String.valueOf(data.get("hr"));
                String spo2 = String.valueOf(data.get("spo2"));

                DataSensor sensorData = new DataSensor(hr, spo2);
                listSensorData.add(sensorData);

                for (FirebaseListener suscriber: _suscribers) {
                    suscriber.onSensorDataFromDbReceived(listSensorData);
                }

                // ...
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                // Log.d("firebase", "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
//                DataSensor sensorData = dataSnapshot.getValue(DataSensor.class);
//                String sensorDataKey = dataSnapshot.getKey();

//                for (FirebaseListener suscriber: _listeners) {
//                    suscriber.onSensorDataFromDbReceived(listSensorData);
//                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("firebase", "postComments:onCancelled", databaseError.toException());
//                Toast.makeText(mContext, "Failed to load comments.",
//                        Toast.LENGTH_SHORT).show();
            }
        };

        memberOxigen.addChildEventListener(childEventListener);

    }


}
