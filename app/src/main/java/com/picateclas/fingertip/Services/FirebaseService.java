package com.picateclas.fingertip.Services;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.picateclas.fingertip.Models.Member;

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
}
