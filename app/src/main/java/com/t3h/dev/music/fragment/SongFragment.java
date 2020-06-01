package com.t3h.dev.music.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import com.t3h.dev.music.R;
import com.t3h.dev.music.activity.MainActivity;
import com.t3h.dev.music.adapter.SongAdapter;
import com.t3h.dev.music.item.ItemSong;
import com.t3h.dev.music.listener.OnPlayMusic;
import com.t3h.dev.music.media.MediaManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class SongFragment extends Fragment {

    private static final String TAG = "SongFragment";
    private ListView lvSong;
    private SongAdapter songAdapter;
    private MainActivity mainActivity;
    private MediaManager mediaManager;
    private static SongFragment songFragment;

    public static SongFragment getInstance() {
        if (songFragment == null) {
            return new SongFragment();
        }
        return songFragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView() - SongFragment");
        View view = inflater.inflate(R.layout.fragment_song, container, false);
        initView(view);
        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mainActivity = (MainActivity) getActivity();
        mediaManager = MediaManager.getInstance(mainActivity);
        songAdapter = new SongAdapter(mainActivity, playSong);

    }

    OnPlayMusic playSong = new OnPlayMusic() {
        @Override
        public void playSong(List<ItemSong> list, int position) {
            mainActivity.showBottomLayout(true);
            mainActivity.setInforBottomLayout(list.get(position).getDisplayName(), list.get(position).getArtist());
            Toast.makeText(getContext(), "Playsong :" + list.size(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated() - SongFragment");
    }

    private void initView(View view) {
        lvSong = view.findViewById(R.id.lv_song);
        lvSong.setAdapter(songAdapter);
    }
}
