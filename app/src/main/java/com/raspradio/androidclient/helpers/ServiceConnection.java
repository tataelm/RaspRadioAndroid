package com.raspradio.androidclient.helpers;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.StrictMode;
import android.util.Log;

import com.raspradio.androidclient.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

import static android.content.Context.WIFI_SERVICE;

public class ServiceConnection extends RadioConnection {

    private Context _context;

    public ServiceConnection(Context applicationContext) {
        this._context = applicationContext;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public boolean connect() {
        try {
            findRadioIp();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void findRadioIp() throws IOException {
        String myIP = getWifiIpAddress();
        myIP = get3BlockOfIP(myIP);
        MainActivity.RADIO_HOST = checkHosts(myIP);
    }

    public boolean manuelRadioHost(String url) {
        System.out.println("Scanning " + url);
        try {
            URL radioHostURL = new URL("http://" + url + "/radio.php/scan");
            BufferedReader in = new BufferedReader(new InputStreamReader(radioHostURL.openStream()));
            String input;
            StringBuffer stringBuffer = new StringBuffer();
            while ((input = in.readLine()) != null) {
                stringBuffer.append(input);
            }
            in.close();
            String htmlData = stringBuffer.toString();

            if (htmlData.equals("true")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    protected String getWifiIpAddress() {
        WifiManager wifiManager = (WifiManager) _context.getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

        // Convert little-endian to big-endianif needed
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

        String ipAddressString;
        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException ex) {
            Log.e("WIFIIP", "Unable to get host address.");
            ipAddressString = null;
        }

        return ipAddressString;
    }

    private String get3BlockOfIP(String myIP) {
        String newstr = "";
        if (null != myIP && myIP.length() > 0) {
            int endIndex = myIP.lastIndexOf(".");
            if (endIndex != -1) {
                newstr = myIP.substring(0, endIndex); // not forgot to put check if(endIndex != -1)
            }
        }
        return newstr;
    }

    private String checkHosts(String subnet) throws IOException {
        String radioHost = "";

        //TODO change i to 1
        for (int i = 0; i < 255; i++) {
            String host = subnet + "." + i;
            System.out.println("Scanning " + host);
            try {

                URL radioHostURL = new URL("http://" + host + "/radio.php/scan");
                BufferedReader in = new BufferedReader(new InputStreamReader(radioHostURL.openStream()));
                String input;
                StringBuffer stringBuffer = new StringBuffer();
                while ((input = in.readLine()) != null) {
                    stringBuffer.append(input);
                }
                in.close();
                String htmlData = stringBuffer.toString();

                if (htmlData.equals("true")) {
                    radioHost = "http://" + host + "/radio.php/";
                    break;
                }
            } catch (Exception ex) {
                String ma = ex.toString();
            }
        }
        return radioHost;
    }


}


