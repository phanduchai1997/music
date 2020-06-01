package com.t3h.dev.music.fragment;


import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import com.t3h.dev.music.R;
import com.t3h.dev.music.activity.DetailArtistActivity;
import com.t3h.dev.music.adapter.SongAdapter;
import com.t3h.dev.music.item.ItemSong;
import com.t3h.dev.music.listener.OnPlayMusic;
import com.t3h.dev.music.media.MediaManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class SongOfArtistFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String TAG = "SongFragment";
    private ListView lvSong;
    private SongAdapter songAdapter;
    private DetailArtistActivity artistActivity;
    private MediaManager mediaManager;
    private static SongOfArtistFragment ongOfArtistFragment;
    private boolean isFirstRun;

    public static SongOfArtistFragment getInstance() {
        if (ongOfArtistFragment == null) {
            return new SongOfArtistFragment();
        }
        return ongOfArtistFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_of_artist, container, false);
        initView(view);
        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.artistActivity = (DetailArtistActivity) getActivity();

        mediaManager = MediaManager.getInstance(artistActivity);
        songAdapter = new SongAdapter(artistActivity, new OnPlayMusic() {
            @Override
            public void playSong(List<ItemSong> list, int position) {
                Toast.makeText(getContext(), "Play album size=:" + list.size() + " in position=" + position, Toast.LENGTH_SHORT).show();
            }
        });
        songAdapter.setArrItemSong(mediaManager.getSongList(MediaStore.Audio.Media.ARTIST_ID + "=?",
                new String[]{artistActivity.getIdArtist() + ""}));
    }

    private void initView(View view) {
        isFirstRun = false;
        lvSong = view.findViewById(R.id.lv_song_of_artist);
        lvSong.setAdapter(songAdapter);
        lvSong.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Show bottom
        if (!isFirstRun) {
            isFirstRun = true;
            artistActivity.showBottomLayout(true);
        }
        artistActivity.setInforBottomLayout(songAdapter.getItem(position).getDisplayName(),
                songAdapter.getItem(position).getArtist());

        //show animation
        songAdapter.updateAnimItem(position);
    }
}
