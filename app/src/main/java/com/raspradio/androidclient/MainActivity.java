package com.raspradio.androidclient;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.raspradio.androidclient.helpers.RadioChannel;
import com.raspradio.androidclient.helpers.RadioConnection;
import com.raspradio.androidclient.helpers.ServiceConnection;
import com.raspradio.androidclient.helpers.StorageHelper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String RADIO_HOST = "";
    public static List<RadioChannel> _listRadioChannels;

    public static ServiceConnection _serviceConnection;
    public static StorageHelper _storageHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


       /* TODO grant permission
       if (ContextCompat.checkSelfPermission(this.getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
        }*/

        _serviceConnection = new ServiceConnection(getApplicationContext());
        _storageHelper = new StorageHelper(getApplicationContext());

        boolean isRadioHostSaved = false;

        try {
            RADIO_HOST = _storageHelper.readRadioHostFromStorage();

            if (!RADIO_HOST.equals(""))
            {
                isRadioHostSaved = true;
            }

        } catch (IOException e) {
           isRadioHostSaved = false;
        }

        if (!isRadioHostSaved)
        {
            scanForRadioHost();
        }
        else
        {
            if(verifyRadioHost())
            {
                initiliazeRadioChannels();
            }
            else
            {
                // TODO must scan again

            }
        }
    }

    /**
     * Check if the saved radio host available
     * */
    private boolean verifyRadioHost() {
        return true; //TODO
    }

    /***
     * Radio hostlarının tarandığı method
     */
    private void scanForRadioHost()  {
        ProgressDialog dialog = ProgressDialog.show(this, "",
                "Scanning for radio hosts. Please wait...", true);

        if (_serviceConnection.connect())
        {
            dialog.dismiss();
            Toast.makeText(this, "Radio host is found - " + RADIO_HOST, Toast.LENGTH_LONG).show();


            try {
                _storageHelper.writeRadioHostToStorage(RADIO_HOST);
            } catch (IOException e) {
                e.printStackTrace();
            }

            initiliazeRadioChannels();

        }
        else
        {
            dialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("No Radio found");
            builder.setMessage("Please write down radio host url to test it or cancel it to try again later \ne.g.: 192.168.43.12");

            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing but close the dialog
                    dialog.dismiss();


                    if (_serviceConnection.manuelRadioHost(input.getText().toString()))
                    {
                        Toast.makeText(getApplicationContext(), "Radio host is found - " + RADIO_HOST, Toast.LENGTH_LONG).show();
                        try {
                            _storageHelper.writeRadioHostToStorage(RADIO_HOST);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        initiliazeRadioChannels();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "No radio host found", Toast.LENGTH_LONG).show();
                    }
                }
            });

            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public static void initiliazeRadioChannels() {
        _listRadioChannels = _serviceConnection.getChannels();
    }

}
