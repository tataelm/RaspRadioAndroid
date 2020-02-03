package com.raspradio.androidclient.helpers;

public class RadioChannel {

    int _radioId;
    String _radioName;
    String _radioURL;
    boolean _isFavourite;

    public RadioChannel()
    {

    }


    public RadioChannel(int radioId, String radioName, String radioUrl, boolean isFavourite)
    {
        this._radioId = radioId;
        this._radioName = radioName;
        this._radioURL = radioUrl;
        this._isFavourite = isFavourite;
    }

    public RadioChannel(String radioName, String radioURL, boolean isFavourite)
    {
        this._radioName = radioName;
        this._radioURL = radioURL;
        this._isFavourite = isFavourite;
    }


    public int get_radioId() {
        return _radioId;
    }

    public void set_radioId(int _radioId) {
        this._radioId = _radioId;
    }

    public String get_radioName() {
        return _radioName;
    }

    public void set_radioName(String _radioName) {
        this._radioName = _radioName;
    }

    public String get_radioURL() {
        return _radioURL;
    }

    public void set_radioURL(String _radioURL) {
        this._radioURL = _radioURL;
    }

    public boolean is_isFavourite() {
        return _isFavourite;
    }

    public void set_isFavourite(boolean _isFavourite) {
        this._isFavourite = _isFavourite;
    }

}
