package com.picateclas.fingertip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
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

public class MainActivity extends AppCompatActivity implements FirebaseListener {

    LinearLayout lyMembers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lyMembers = findViewById(R.id.lyMembers);

        FileService fileService = new FileService(this);
        FirebaseService.getInstance().suscribe(this);

        if (fileService.verifyIfFileExists())
            FirebaseService.getInstance().getMembersDataBase(this);


    }

    public void addMember(View view) {
        Intent intent = new Intent(this, AddMemberActivity.class);
        startActivity(intent);
    }

    public void submitMember(View view) {

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

    public void testVerifyId(View view) {
        // esto llama a la verificacion de un id, lo puse aqui para hacer la prueba al presionar un boton xD
        FirebaseService.getInstance().verifyIfIdExists("asdfaffs");
    }

    /*
    * Seleccionar los miembros a compartir
    * y guardarlos en un List<Member>
    * enviar la lista a ts.sendTelegramIds(this, members <- aqui xd );
     */

    /*
    * Para "importar" ids (agregar miembros mediante el id que alguien mas te pasa)
    * un campo para pegar el texto, los ids deberían estar separados por enters (\n), como lo enviamos a telegram,
    * asi que habria que separar la cadena y crear un array, quiza, o una lista o algo xD para iterar en los ids
    * y verificar si existen, para verificar que un id existe solo llaman a
    * FirebaseService.getInstance().verifyIfIdExists("asdfaffs");
    * y le pasan el id como string en lugar de asdfaffs xD
    * cuando se verifique, se ejecutará el método onIdVerificationCompleted
    * que recibe el resultado (idExists), es true si el id existe, false si no existe
    * Si el id existe, se añade al archivo de texto, para eso llaman a addMemberIdToFile(String memberId),
    * de FileService, pero sería bueno que antes de agregarlo al archivo también verifiquen
    * si es que ya existe en el archivo (para no tener duplicados),
    * el método getMembersIds(Context context) de FileService les devuelve una lista de strings con los ids
    * que ya estan registrados, entonces, si es que el id existe dentro de firebase y si es que aún no
    * está guardado en el archivo, se lo guarda con addMemberIdToFile(String memberId).
    *
    *
    * NO SÉ SI ESTO DEBA IR EN EL MAIN ACTIVITY O EN OTRA ACTIVITY O UNA VENTANA POP UP O KHEEE
    * Pero ustedes vean eso xD el caso es que estas cosas funcionen kljsfjdfd
    * Sólo que si utilizan otra clase asegúrense de que implemente la interfaz FirebaseListener y sus
    * métodos, el onMembersReceived lo pueden dejar vacio xd
    * y al implementarlo, en el contructor se deben suscribir al FirebaseService, con
    * FirebaseService.getInstance().suscribe(this);
    * y así les funcionará, eso si es que implementan esto en una clase aparte, como les dije
    * ustedes vean qué es lo mejor xdxd
    * */

    /*
    * evento onRestore (cuando vas a otra activity y vuelves)
    * para volver a cargar los miembros, solo hace falta llamar a
    * FirebaseService.getInstance().getMembersDataBase(this);
     */

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    // método heredado de FirebaseListener
    @Override
    public void onMembersReceived(List<Member> members) {

        for (Member member: members) {
            // mostrar los miembros en la interfaz
            //etMembers.append(member.getName() + "\n");

            Button btn = new Button(this);
            btn.setText(member.getName());
            btn.setOnClickListener((v -> {
                Intent intent = new Intent(this, activity_menu.class);
                Bundle bundle = new Bundle();
                bundle.putString("memberId",member.getId());
                startActivity(intent);

            }));

            lyMembers.addView(btn);
        }
    }

    // método heredado de FirebaseListener
    @Override
    public void onIdVerificationCompleted(boolean idExists) {
        // este metodo se llama cuando un id no existe, la verificacion se hace
        // cuando "importamos" ids, al pegar la lista de ids.
        Toast.makeText(
                this,
                "Uno de los identificadores que ingresaste no es correcto, verifica los datos e intentalo de nuevo :)",
                Toast.LENGTH_LONG)
                .show();
    }


}