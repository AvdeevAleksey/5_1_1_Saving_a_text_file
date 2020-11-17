package ru.avdeev.android.a5_1_1_saving_a_text_file;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ExternalFile {
    Activity activity;
    String fileName;

    public static final int REQUEST_CODE_PERMISSION_READ_STORAGE = 10;
    public static final int REQUEST_CODE_PERMISSION_WRITE_STORAGE = 11;


    public ExternalFile(Activity activity, String fileName) {
        this.fileName = fileName;
        this.activity = activity;

        int permissionStatus = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionStatus == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION_WRITE_STORAGE);
        }

    }

    public boolean saveStringList(List<String> list) {
        if (isExternalStorageWritable()) {
            File fileToSave = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
            FileWriter fileWriter;
            try {
                fileWriter = new FileWriter(fileToSave, false);

                for (String s : list) {
                    fileWriter.append(String.format("%s;", s));
                }
                fileWriter.close();
                Toast.makeText(activity, activity.getString(R.string.file_saved, fileToSave.getAbsolutePath()),
                        Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(activity, activity.getString(R.string.file_save_error, fileToSave.getAbsolutePath()),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public List<String> loadStringList() {
        List<String> list;
        File fileToLoad = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
        try {
            Scanner scanner = new Scanner(fileToLoad);
            list = Arrays.asList(scanner.nextLine().split(";"));
        } catch (FileNotFoundException e) {
            list = null;
        }
        return list;
    }


    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }


}
