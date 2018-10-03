package com.omelchenkoaleks.writeexternal;

import android.Manifest;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    // разрешение для API = и выше 23
    private static final int PERMISSION_CODE = 23;

    private static final String TAG_FILE = "TAG_FILE";
    EditText etComments;
    Button btWrite;
    Button btRead;
    String komments;
    File folder;
    File file;
    FileInputStream fileInputStream;
    InputStreamReader inputStreamReader;
    BufferedReader bufferedReader;
    FileOutputStream fileOutputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etComments = findViewById(R.id.et_comments);
        btWrite = findViewById(R.id.bt_write);
    }

    public void writeExternalPublic(View view) {
        // разрешение на доступ к хранилищу
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CODE);
        }

        // проверка наличия внешнего хранилища, возможности записи
        if (isExternalStorageWritable() || isExternalStorageReadable()) {
            komments = etComments.getText().toString();
            // имя папки
            folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            // имя файла
            file = new File(folder, "data_public.txt");
            writeData(file, komments);
            etComments.setText("");
        } else {
            btWrite.setEnabled(false);
            Toast.makeText(this, "only reading", Toast.LENGTH_SHORT).show();
        }
    }
    private void writeData(File file, String data) {
        fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(data.getBytes());
            Toast.makeText(this, "saved: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (Exception exception) {
            Log.e(TAG_FILE, exception.getMessage(), exception);
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException exception) {
                    Log.e(TAG_FILE, exception.getMessage(), exception);
                }
            }
        }
    }

    // проверка внешнего хранилища на чтение и запись
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    // проверка внешнего хранилища на возможность только чтения
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

//    private String readData(File file) {
//        fileInputStream = null;
//        try {
//            fileInputStream = new FileInputStream(file);
//            inputStreamReader = new InputStreamReader(fileInputStream);
//            bufferedReader = new BufferedReader(inputStreamReader);
//            int i;
//            StringBuilder stringBuilder = new StringBuilder();
//
//            // считываем байты данных из входного потока
//            // отсутствие байтов возвращает -1
//            while ((i = bufferedReader.read()) != -1) {
//                stringBuilder.append((char) i);
//            }
//            return stringBuilder.toString();
//        } catch (Exception exception) {
//            Log.e(TAG_FILE, exception.getMessage(), exception);
//        } finally {
//            if (fileInputStream != null) {
//                try {
//                    fileInputStream.close();
//                }catch (IOException exception) {
//                    Log.e(TAG_FILE, exception.getMessage(), exception);
//                }
//            }
//        }
//    }
}
