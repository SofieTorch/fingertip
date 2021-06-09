package com.picateclas.fingertip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.picateclas.fingertip.Interfaces.FirebaseListener;
import com.picateclas.fingertip.Models.Member;
import com.picateclas.fingertip.Services.FirebaseService;
import com.picateclas.fingertip.Services.TelegramService;

import java.util.List;

public class MainActivity extends AppCompatActivity implements FirebaseListener {
    EditText etMembersTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseService.getInstance().suscribe(this);
        etMembersTest = findViewById(R.id.etMembersTest);
    }

    public void addMember(View view) {
        Intent intent = new Intent(this, AddMemberActivity.class);
        startActivity(intent);
    }

    public void testSentMessage(View view) {
        TelegramService ts = new TelegramService();
        ts.intentMessageTelegram(this, "El Andy nos traicionó");
    }

    public void getDataTest(View view) {
        FirebaseService.getInstance().getMembersDataBase(this);
    }

    @Override
    public void onMembersReceived(List<Member> members) {
        for (Member member: members) {
            etMembersTest.append(member + "\n");
        }
    }
}