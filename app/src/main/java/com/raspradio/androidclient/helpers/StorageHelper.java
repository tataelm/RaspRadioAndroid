package com.raspradio.androidclient.helpers;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.os.ParcelFileDescriptor.MODE_WORLD_READABLE;

public class StorageHelper {

    Context _context;
    String _fileName = "radioHost";
    private File _path;

    public StorageHelper(Context context)
    {
        this._context = context;

       // _path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + _fileName;
        _path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

       /* File dir = new File(_path);
        if (!dir.exists()) {
            dir.mkdirs();
        }*/
    }


    public void writeRadioHostToStorage(String url) throws IOException {
        String path = Environment.getExternalStorageDirectory() + "/" + "RaspRadio.txt";
        File file = new File(path);
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        writer.write(url);
        writer.close();
    }


    public String readRadioHostFromStorage() throws IOException {
     /*   FileInputStream fis = _context.openFileInput("");
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader bufferedReader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();*/
        String path = Environment.getExternalStorageDirectory() + "/" + "RaspRadio.txt";
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(path )));
        String read;
        StringBuilder builder = new StringBuilder("");

        while((read = bufferedReader.readLine()) != null){
            builder.append(read);
        }
        Log.d("Output", builder.toString());
        bufferedReader.close();

        return builder.toString();
    }

}
