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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FirebaseListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseService.getInstance().suscribe(this);
        FirebaseService.getInstance().getMembersDataBase(this);
    }

    public void addMember(View view) {
        Intent intent = new Intent(this, AddMemberActivity.class);
        startActivity(intent);
    }

    public void testSentMessage(View view) {
        TelegramService ts = new TelegramService();

        List<Member> members = Arrays.asList(
            new Member("Dani", 19, "-MbhlRg9v1qW6WDituns"),
            new Member("Vale", 19, "-MbikhlWtSKnZrvGvE14")
        );

        ts.sendTelegramIds(this, members);
    }

    /*
    * Seleccionar los miembros a compartir
    * y guardarlos en un List<Member>
    * enviar la lista a ts.sendTelegramIds(this, members <- aqui xd );
     */

    /*
    * evento onRestore (cuando vas a otra activity y vuelves)
    * para volver a cargar los miembros, solo hace falta llamar a
    * FirebaseService.getInstance().getMembersDataBase(this);
     */


    @Override
    public void onMembersReceived(List<Member> members) {
        for (Member member: members) {
            // mostrar los miembros en la interfaz
        }
    }
}