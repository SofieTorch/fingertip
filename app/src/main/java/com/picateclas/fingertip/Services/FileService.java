package com.picateclas.fingertip.Services;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class FileService {
    private static final String _fileName = "MembersList.txt";
    private final Context context;

    public FileService(Context context) {
        this.context = context;
        createFile();
    }

    public boolean verifyIfFileExists() {
        boolean res = false;
        String[] files = this.context.fileList();
        for (String file : files) {
            if (_fileName.equals(file)) {
                res = true;
                break;
            }
        }

        return res;
    }

    private void createFile() {
        if (!verifyIfFileExists()){
            try {
                FileOutputStream fileOutputStream = context.openFileOutput(_fileName, Context.MODE_PRIVATE);
                fileOutputStream.close();
            }
            catch (IOException e) {
                Log.e("FileService", e.getMessage());
            }
        }
    }

    public boolean addMemberIdToFile(String memberId) {
        OutputStreamWriter outputStreamWriter = null;
        try {
            outputStreamWriter = new OutputStreamWriter(context.openFileOutput(_fileName, Context.MODE_APPEND));
            outputStreamWriter.write(memberId + "\n");
            outputStreamWriter.flush();
            outputStreamWriter.close();
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            Log.e("FileService", e.getMessage());
            return false;
        }
    }

    public List<String> getMembersIds() {

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(context.openFileInput(_fileName));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String linea = bufferedReader.readLine();
            HashSet<String> membersIds = new HashSet<String>();

            while(linea != null){
                membersIds.add(linea);
                linea = bufferedReader.readLine();
            }

            return new ArrayList<String>(membersIds);

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
