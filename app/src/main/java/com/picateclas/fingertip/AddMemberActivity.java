package com.picateclas.fingertip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.picateclas.fingertip.Models.Member;
import com.picateclas.fingertip.Services.FileService;
import com.picateclas.fingertip.Services.FirebaseService;

public class AddMemberActivity extends AppCompatActivity {

    EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        etName = findViewById(R.id.etName);
    }

    public void register(View view) {
        String name = etName.getText().toString().trim();
        int age;

        // valida si los campos estan vacios
        if (name.isEmpty()) {
            Toast.makeText(this,
                    "Debe todos los campos",
                    Toast.LENGTH_LONG)
                    .show();
        }
        else {
            // age = Integer.parseInt(ageStr);
            // String memberId = registerToDatabase(name, age);
            // saveMemberId(memberId);
        }
    }

    public void cancel(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // registra a la base de datos
    private String registerToDatabase(String name, int age) {
        // miembro a registrar
        Member member = new Member(name, age);
        return FirebaseService.getInstance().registerNewMember(member);
    }

    private void saveMemberId(String memberId) {
        FileService fs = new FileService(this);
        boolean success = fs.addMemberIdToFile(memberId);

        if (!success) {
            Toast.makeText(this,
                    "Ups! no se pudo guardar el registro, inténtelo otra vez.",
                    Toast.LENGTH_LONG)
                    .show();
        }
        else {
            Toast.makeText(this,
                    "Registro exitoso! :)",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }


}