package com.raspradio.androidclient.ui.radiocontrol;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.raspradio.androidclient.MainActivity;
import com.raspradio.androidclient.R;
import com.raspradio.androidclient.helpers.RadioChannel;

public class RadioControlFragment extends Fragment {

    private RadioControlViewModel radioControlViewModel;

    TextView txtChoosenChannel;
    Button btnPlayPause, btnVolumeUp, btnVolumeDown, btnMute;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        radioControlViewModel =
                ViewModelProviders.of(this).get(RadioControlViewModel.class);
        View root = inflater.inflate(R.layout.fragment_radiocontrol, container, false);

      txtChoosenChannel = root.findViewById(R.id.txtChoosenChannel);
      btnPlayPause = root.findViewById(R.id.btnPlay);
      btnMute = root.findViewById(R.id.btnMute);
      btnVolumeUp = root.findViewById(R.id.btnVolumeUp);
      btnVolumeDown = root.findViewById(R.id.btnVolumeDown);


        RadioChannel selectedChannel = MainActivity._serviceConnection.getSelectedChannel();

        txtChoosenChannel.setText(selectedChannel.get_radioName());

        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity._serviceConnection.playPause();
            }
        });


        btnVolumeUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity._serviceConnection.volumeUp();
            }
        });

        btnVolumeDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity._serviceConnection.volumeDown();
            }
        });

        btnMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity._serviceConnection.mute();
            }
        });

        return root;
    }
}