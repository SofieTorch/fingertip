package com.picateclas.fingertip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.picateclas.fingertip.Services.FirebaseService;
import com.picateclas.fingertip.Services.TelegramService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}