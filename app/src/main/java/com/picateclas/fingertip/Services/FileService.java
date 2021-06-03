package com.picateclas.fingertip.Services;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;


public class FileService {
    private final String _fileName = "MembersList.txt";
    private final Context context;

    public FileService(Context context) {
        this.context = context;
        createFile(_fileName);
    }

    private boolean verifyIfFileExists(String fileName) {
        boolean res = false;
        String[] files = this.context.fileList();
        for (String file : files) {
            if (fileName.equals(file)) {
                res = true;
                break;
            }
        }

        return res;
    }

    private void createFile(String fileName) {
        if (!verifyIfFileExists(fileName)){
            try {
                FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
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
            outputStreamWriter.write(memberId + ",");
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

}
