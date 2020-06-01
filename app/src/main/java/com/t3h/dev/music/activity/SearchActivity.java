package com.t3h.dev.music.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;

import com.t3h.dev.music.R;
import com.t3h.dev.music.adapter.SongAdapter;
import com.t3h.dev.music.item.ItemSong;
import com.t3h.dev.music.listener.OnPlayMusic;
import com.t3h.dev.music.media.MediaManager;

public class SearchActivity extends AppCompatActivity {

    private ImageView imvBack;
    private EditText editInput;
    private ListView lvSearch;
    private SongAdapter songAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView() {
        imvBack = findViewById(R.id.imv_back_search);
        imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lvSearch = findViewById(R.id.lv_search);
        songAdapter = new SongAdapter(this, onPlayMusic);
        songAdapter.setArrItemSong(MediaManager.getInstance(this).getSongList(null, null));
        lvSearch.setAdapter(songAdapter);

        editInput = findViewById(R.id.edt_search);
        editInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String textFill = s.toString();
                songAdapter.setArrSong(songAdapter.pushData());
                songAdapter.filter(textFill);
                songAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    songAdapter.clearArr();
                    songAdapter.setArrItemSong(songAdapter.pushData());
                    songAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    OnPlayMusic onPlayMusic = new OnPlayMusic() {
        @Override
        public void playSong(List<ItemSong> list, int position) {
        }
    };

}
