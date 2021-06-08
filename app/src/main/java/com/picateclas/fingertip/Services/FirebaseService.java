package com.picateclas.fingertip.Services;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.picateclas.fingertip.Models.Member;

import java.util.ArrayList;
import java.util.List;

public class FirebaseService {
    private final FirebaseDatabase _database;
    private static FirebaseService _instance;

    private FirebaseService() {
        _database = FirebaseDatabase.getInstance();
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

    // void -> List<Member>
    public void getMembers(Context context) {
        List<String> membersIds = FileService.getMembersIds(context);
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
                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    }
                }

            });
        }

    }
}
