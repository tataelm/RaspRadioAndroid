package com.raspradio.androidclient.helpers;

import android.content.Context;
import android.provider.MediaStore;

import com.raspradio.androidclient.MainActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RadioConnection {

    public List<RadioChannel> getChannels()
    {
        List<RadioChannel> listRadioChannels = new ArrayList<>();

        try {

            URL radioHostURL = new URL(MainActivity.RADIO_HOST + "getChannels");
            BufferedReader in = new BufferedReader(new InputStreamReader(radioHostURL.openStream()));
            String input;
            StringBuffer stringBuffer = new StringBuffer();
            while ((input = in.readLine()) != null) {
                stringBuffer.append(input);
            }
            in.close();
            String allChannelsString = stringBuffer.toString();

            listRadioChannels = prepareChannelList(allChannelsString, listRadioChannels);
            return listRadioChannels;

        } catch (Exception ex) {
            return listRadioChannels;
        }
    }

    public RadioChannel getSelectedChannel()
    {
        //http://10.0.0.86/radio.php/getSelectedChannel
        RadioChannel selectedChannel = new RadioChannel();
        List<RadioChannel> listRadioChannels = new ArrayList<>();

        try {

            URL radioHostURL = new URL(MainActivity.RADIO_HOST + "getSelectedChannel");
            BufferedReader in = new BufferedReader(new InputStreamReader(radioHostURL.openStream()));
            String input;
            StringBuffer stringBuffer = new StringBuffer();
            while ((input = in.readLine()) != null) {
                stringBuffer.append(input);
            }
            in.close();
            String selectedChannelString = stringBuffer.toString();
            listRadioChannels = prepareChannelList(selectedChannelString, listRadioChannels);

            return listRadioChannels.get(0);

        } catch (Exception ex) {
            return selectedChannel;
        }
    }


    private List<RadioChannel> prepareChannelList(String allChannelsString, List<RadioChannel> listRadioChannel) {

        List<RadioChannel> mListRadioChannel = listRadioChannel;
        String[] channelsArray = allChannelsString.split("<br>"); // satırlara bölündü

        for (String channels : channelsArray) // her satıra bakıyor
        {
            RadioChannel rc = new RadioChannel();
            String[] channelDetails = channels.split("\\|");

            for (String seperateDetails : channelDetails)
            {
                String first2Chars = seperateDetails.trim().substring(0, 2);
                String detail = seperateDetails.substring(seperateDetails.indexOf(":") + 1).trim();

                switch (first2Chars)
                {
                    case "id":
                        rc.set_radioId(Integer.parseInt(detail));
                        break;

                    case "Na":
                        rc.set_radioName(detail);
                        break;

                    case "UR":
                        rc.set_radioURL(detail);
                        break;

                    case "is":
                        if (detail.equals("0"))
                        {
                            rc.set_isFavourite(false);
                        }
                        else
                        {
                            rc.set_isFavourite(true);
                        }
                        break;
                }
            }
            mListRadioChannel.add(rc);
        }
        return mListRadioChannel;
    }

    public void changeChannel(int position) {
        try {

            URL radioHostURL = new URL(MainActivity.RADIO_HOST + "changeChannel/" + position);
            radioHostURL.openStream();
        } catch (Exception ex) {
        }
    }

    public void addChannel(String radioName, String radioURL)
    {
        try {
            URL radioHostURL = new URL(MainActivity.RADIO_HOST + "addChannel/" + radioName + "/" + radioURL);
            radioHostURL.openStream();
        } catch (Exception ex) {
        }
    }

    public void deleteChannel(int channelId)
    {
        try {
            URL radioHostURL = new URL(MainActivity.RADIO_HOST + "deleteChannel/" + channelId);
            radioHostURL.openStream();
        } catch (Exception ex) {
        }
    }

    public void playPause()
    {
        try {

            URL radioHostURL = new URL(MainActivity.RADIO_HOST + "turnOnOff");
            radioHostURL.openStream();
        } catch (Exception ex) {
        }
    }

    public void mute()
    {
        try {
            URL radioHostURL = new URL(MainActivity.RADIO_HOST + "mute");
            radioHostURL.openStream();
        } catch (Exception ex) {
        }
    }

    public void volumeUp()
    {
        try {

            URL radioHostURL = new URL(MainActivity.RADIO_HOST + "volumeUp");
            radioHostURL.openStream();
        } catch (Exception ex) {
        }
    }

    public void volumeDown()
    {
        try {

            URL radioHostURL = new URL(MainActivity.RADIO_HOST + "volumeDown");
            radioHostURL.openStream();
         } catch (Exception ex) {
        }
    }

    public void setUnsetFavourite(int channelId)
    {
        try {
            URL radioHostURL = new URL(MainActivity.RADIO_HOST + "setUnsetFavourites/" + channelId);
            radioHostURL.openStream();
        } catch (Exception ex) {
        }
    }


}
