package com.t3h.dev.music.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.t3h.dev.music.R;
import com.t3h.dev.music.activity.DetailArtistActivity;
import com.t3h.dev.music.activity.MainActivity;
import com.t3h.dev.music.adapter.ArtistAdapter;
import com.t3h.dev.music.common.Const;
import com.t3h.dev.music.media.MediaManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArtistFragment extends Fragment implements AdapterView.OnItemClickListener {


    private static final String TAG = "ArtistFragment";
    private static ArtistFragment artistFragment;
    private MainActivity mainActivity;

    //initView
    private ListView lvItemArtist;
    private ArtistAdapter artistAdapter;

    public static ArtistFragment getInstance() {
        if (artistFragment == null) {
            return new ArtistFragment();
        }
        return artistFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView() - " + TAG);
        View view = inflater.inflate(R.layout.fragment_artist, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        lvItemArtist = view.findViewById(R.id.lv_item_artist);
        lvItemArtist.setAdapter(artistAdapter);
        lvItemArtist.setOnItemClickListener(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate() - " + TAG);
        this.mainActivity = (MainActivity) getActivity();
        artistAdapter = new ArtistAdapter(mainActivity);
        artistAdapter.setArrItemArtist(MediaManager.getInstance(mainActivity).getAllArtist());
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        actionShowDetailArtist(artistAdapter.getItem(position).getIdArtist(), artistAdapter.getItem(position).getArtistName());
    }

    private void actionShowDetailArtist(int idArtist, String artistName) {
        Intent intent = new Intent(mainActivity, DetailArtistActivity.class);
        intent.putExtra(Const.KEY_ID_ARTIST, idArtist);
        intent.putExtra(Const.KEY_NAME_ARTIST, artistName);
        startActivity(intent);
    }
}
