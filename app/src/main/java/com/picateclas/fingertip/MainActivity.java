package com.picateclas.fingertip;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.picateclas.fingertip.Interfaces.FirebaseListener;
import com.picateclas.fingertip.Models.DataSensor;
import com.picateclas.fingertip.Models.Member;
import com.picateclas.fingertip.Services.FileService;
import com.picateclas.fingertip.Services.FirebaseService;
import com.picateclas.fingertip.Services.TelegramService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FirebaseListener, PopupMenu.OnMenuItemClickListener {

    LinearLayout lyMembers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lyMembers = findViewById(R.id.lyMembers);
        FirebaseService.getInstance().suscribe(this);

    }


    private void loadMembers() {
        FileService fileService = new FileService(this);
        if (fileService.verifyIfFileExists())
            FirebaseService.getInstance().getMembersDataBase(this);
    }


    public void addMember(View view) {
        Intent intent = new Intent(this, AddMemberActivity.class);
        startActivity(intent);
    }


    public void showTelegramActionsMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.telegram_actions);
        popup.show();
    }


    public void openTelegramDialogToAddMember() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Importar miembros");
        alertDialog.setMessage("Inserte el mensaje de Telegram con los identificadores que le mandaron :)");

        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        // alertDialog.setIcon(R.drawable.key);

        alertDialog.setPositiveButton("Agregar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String[] membersIdsArray = input.getText().toString().split("\n");
                        for (String memberId : membersIdsArray) {
                            FirebaseService.getInstance().verifyIfIdExists(memberId);
                        }
                    }
                });

        alertDialog.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadMembers();
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shareMembers:
                Intent intent = new Intent(this, checkMembers.class);
                startActivity(intent);
                return true;
            case R.id.importMembers:
                openTelegramDialogToAddMember();
                return true;
            default:
                return false;
        }
    }


    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    public void onMembersReceived(List<Member> members) {
        lyMembers.removeAllViews();

        for (Member member: members) {

            Button btn = new Button(this);

            btn.setText(member.getName());
            btn.setBackground(getResources().getDrawable(R.drawable.member_button_shape));
            btn.setTextColor(getResources().getColor(R.color.black));
            btn.setGravity(Gravity.CENTER_VERTICAL);
            btn.setAllCaps(false);

            btn.setOnClickListener((v -> {
                Intent intent = new Intent(this, activity_menu.class);
                Bundle bundle = new Bundle();
                bundle.putString("memberId",member.getId());
                bundle.putString("memberName",member.getName());
                intent.putExtras(bundle);
                startActivity(intent);

            }));

            lyMembers.addView(btn);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) btn.getLayoutParams();
            params.setMargins(0 ,4, 0, 8);
            btn.setLayoutParams(params);

            btn.setPadding(56, 8, 0, 8);
            btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_member_resized, 0, 0, 0);
            btn.setCompoundDrawablePadding(14);
        }
    }


    @Override
    public void onIdVerificationCompleted(boolean idExists, String memberId) {
        if (idExists) {
            FileService fs = new FileService(this);
            fs.addMemberIdToFile(memberId);
            loadMembers();
        }
        else {
            Toast.makeText(
                this,
                "Uno de los identificadores que ingresaste no es correcto, verifica los datos e intentalo de nuevo :)",
                Toast.LENGTH_LONG)
                .show();
        }
    }

    @Override
    public void onSensorDataFromDbReceived(List<DataSensor> data) { }
}