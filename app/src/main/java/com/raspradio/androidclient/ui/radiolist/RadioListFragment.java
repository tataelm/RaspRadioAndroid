package com.raspradio.androidclient.ui.radiolist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.raspradio.androidclient.MainActivity;
import com.raspradio.androidclient.R;
import com.raspradio.androidclient.helpers.RadioChannel;
import com.raspradio.androidclient.helpers.RadioListAdapter;

public class RadioListFragment extends Fragment {

    private RadioListViewModel radioListViewModel;
    ListView listViewRadioChannels;
    RadioListAdapter _adapter;

    MenuItem showFavsMenuItem;

    public static boolean _showOnlyFavourites = false;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        radioListViewModel =
                ViewModelProviders.of(this).get(RadioListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_radiolist, container, false);

       listViewRadioChannels = root.findViewById(R.id.listViewRadioChannels);

       _adapter = new RadioListAdapter(getActivity(),R.layout.listrow_radio,MainActivity._listRadioChannels);
       listViewRadioChannels.setAdapter(_adapter);


       listViewRadioChannels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               int radioID = MainActivity._listRadioChannels.get(position).get_radioId();
               MainActivity._serviceConnection.changeChannel(radioID);
           }
       });

       // ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, MainActivity._listRadioChannels);

        registerForContextMenu(listViewRadioChannels);
        setHasOptionsMenu(true);


        return root;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId()==R.id.listViewRadioChannels) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.radiolist_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final RadioChannel rc = MainActivity._listRadioChannels.get(info.position);
        final int radioID = rc.get_radioId();

        switch(item.getItemId()) {

            case R.id.menu_favourite:

                MainActivity._serviceConnection.setUnsetFavourite(radioID);

                if(rc.is_isFavourite())
                {
                    rc.set_isFavourite(false);
                }
                else
                {
                    rc.set_isFavourite(true);
                }

                _adapter.notifyDataSetChanged();

                return true;

            case R.id.menu_delchannel:



                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                MainActivity._serviceConnection.deleteChannel(radioID);
                                MainActivity._listRadioChannels.remove(rc);
                                _adapter.notifyDataSetChanged();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure want to delete this channel?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();


                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }


    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.radiolist_actionbar, menu);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                showFavsMenuItem = menu.findItem(R.id.showfavouritesonly);
                // view.startAnimation(animation);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.showfavouritesonly:
                _showOnlyFavourites = !_showOnlyFavourites;
                _adapter.notifyDataSetChanged();

                if (_showOnlyFavourites)
                {
                    showFavsMenuItem.setTitle("SHOW ALL");
                }
                else
                {
                    showFavsMenuItem.setTitle("SHOW ONLY FAVOURITES");
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}


