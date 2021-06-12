package com.picateclas.fingertip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.picateclas.fingertip.Interfaces.FirebaseListener;
import com.picateclas.fingertip.Models.Member;
import com.picateclas.fingertip.Services.FileService;
import com.picateclas.fingertip.Services.FirebaseService;
import com.picateclas.fingertip.Services.TelegramService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class checkMembers extends AppCompatActivity implements FirebaseListener{


    LinearLayout lyCheckMembers;
    private List<Member> membersList =  new ArrayList<Member>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_members);

        lyCheckMembers = findViewById(R.id.lyCheckMembers);

        FileService fileService = new FileService(this);
        FirebaseService.getInstance().suscribe(this);

        if (fileService.verifyIfFileExists())
            FirebaseService.getInstance().getMembersDataBase(this);
    }


    @Override
    public void onMembersReceived(List<Member> members) {
        for (Member member: members) {
            // mostrar los miembros en la interfaz
            //etMembers.append(member.getName() + "\n");

            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(member.getName());
            checkBox.setTextSize(20);
            checkBox.setTextColor(Color.rgb(4,36,68));


            checkBox.setOnClickListener((v -> {
                if (checkBox.isChecked())
                {
                    membersList.add(new Member(member.getName(), member.getAge(), member.getId()));
                }
            }));
            lyCheckMembers.addView(checkBox);

        }
    }

    @Override
    public void onIdVerificationCompleted(boolean idExists, String memberId) {

    }

    public void sendThroughTelegram(View view) {
        TelegramService ts = new TelegramService();
        ts.sendTelegramIds(this, membersList);
    }
}