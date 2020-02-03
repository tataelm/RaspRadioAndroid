package com.raspradio.androidclient.helpers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.raspradio.androidclient.R;
import com.raspradio.androidclient.ui.radiolist.RadioListFragment;

import java.util.ArrayList;
import java.util.List;

public class RadioListAdapter extends ArrayAdapter<RadioChannel> {

    private Context _context;
    int _resource;


    public RadioListAdapter(Context context, int resource, List<RadioChannel> objects) {
        super(context, resource, objects);
        _context = context;
        _resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

       String radioName = getItem(position).get_radioName();
       String radioURL = getItem(position).get_radioURL();
       boolean isFavourite = getItem(position).is_isFavourite();

       RadioChannel rc = new RadioChannel(radioName,radioURL,isFavourite);

        LayoutInflater inflater = LayoutInflater.from(_context);
        convertView = inflater.inflate(_resource, parent, false);

        TextView txtRadioName = convertView.findViewById(R.id.txtRadioName);
        TextView txtRadioUrl = convertView.findViewById(R.id.txtRadioUrl);
        ImageView txtRadioFavourite = convertView.findViewById(R.id.imgFavourite);

        txtRadioName.setText(radioName);
        txtRadioUrl.setText(radioURL);

        if (isFavourite)
        {
            txtRadioFavourite.setImageResource(R.drawable.star_yellow);
        }
        else
        {
            txtRadioFavourite.setImageResource(R.drawable.star_white);

            if (RadioListFragment._showOnlyFavourites)
            {
                convertView.setLayoutParams(new AbsListView.LayoutParams(-1,1));
                convertView.setVisibility(View.GONE);
            }
        }
        return convertView;
       // return super.getView(position, convertView, parent);
    }
}
