package com.raspradio.androidclient.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.raspradio.androidclient.MainActivity;
import com.raspradio.androidclient.R;

import java.io.IOException;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;

    EditText txtUrl, txtRadioUrl, txtRadioName;
    Button btnSaveUrl, btnAddRadio, btnScanChannels, btnScanRadio;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        txtUrl = root.findViewById(R.id.edittextUrl);
        txtRadioName = root.findViewById(R.id.edittextRadioName);
        txtRadioUrl = root.findViewById(R.id.edittextRadioUrl);

        btnAddRadio = root.findViewById(R.id.btnAddRadio);
        btnSaveUrl = root.findViewById(R.id.btnSaveUrl);
        btnScanChannels = root.findViewById(R.id.btnScanChannels);
        btnScanRadio = root.findViewById(R.id.btnScanRadio);

        String radioUrl = "";
        try {
            radioUrl = MainActivity._storageHelper.readRadioHostFromStorage();
        } catch (IOException e) {
            e.printStackTrace();
        }

        txtUrl.setText(radioUrl);


        btnSaveUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnAddRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mUrl = txtRadioUrl.getText().toString();
                mUrl= mUrl.replace("/", "@");

                String mName = txtRadioName.getText().toString();
                mName = mName.replace(" ", "_");

                MainActivity._serviceConnection.addChannel(mName,mUrl);


                Toast.makeText(getContext(), "Adding radio was successfull", Toast.LENGTH_SHORT).show();

                MainActivity.initiliazeRadioChannels();

                txtRadioName.setText("");
                txtRadioUrl.setText("");
            }
        });


        btnScanChannels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.initiliazeRadioChannels();
                Toast.makeText(getContext(), "Scan was successfull", Toast.LENGTH_SHORT).show();
            }
        });

        btnScanRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity._serviceConnection.connect();
            }
        });


        return root;
    }
}