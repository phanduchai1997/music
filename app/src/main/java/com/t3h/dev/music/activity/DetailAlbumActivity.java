package com.t3h.dev.music.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.t3h.dev.music.R;
import com.t3h.dev.music.adapter.TrackAdapter;
import com.t3h.dev.music.common.Const;
import com.t3h.dev.music.common.SharePreferencesController;
import com.t3h.dev.music.item.ItemSong;
import com.t3h.dev.music.listener.OnPlayMusic;
import com.t3h.dev.music.media.MediaManager;

public class DetailAlbumActivity extends Activity implements View.OnClickListener {

    private ImageView imvBack;
    private TextView tvSeach, tvSetting;
    private TextView tvTitleAlbums, tvInforAlbum;
    private TextView tvRandomList;
    private ListView lvListSong;

    //Bottom menu
    private View viewBottom;
    private LinearLayout llDetailTitleSong;
    private ImageView imvSong;
    private ImageView imvPausePlay;
    private ImageView imvPrevious, imvNext;
    private TextView tvTitleSong;
    private TextView tvNameArtist;

    //
    private int albumID;
    private String albumName;
    private String albumArtist;
    private ArrayList<ItemSong> arrItemTrack = new ArrayList<>();
    private Handler handler = new Handler();
    //
    private TrackAdapter trackAdapter;
    private boolean isFirstRun;
    private boolean isPauseOrPlay;

    private MediaManager mediaManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_album);
        getData();
        initView();
        mediaManager = MediaManager.getInstance(this);
        DetailAlbumActivity.this.runOnUiThread(runnable);
    }

    private void initView() {
        isPauseOrPlay = false;
        //init action
        imvBack = findViewById(R.id.imv_back);
        imvBack.setOnClickListener(this);
        tvSeach = findViewById(R.id.tv_action_search);
        tvSetting = findViewById(R.id.tv_action_setting);
        tvSeach.setOnClickListener(this);
        tvSetting.setOnClickListener(this);

        tvTitleAlbums = findViewById(R.id.tv_title_album_detail);
        tvTitleAlbums.setText(albumName);
        tvInforAlbum = findViewById(R.id.tv_infor_detail_album);
        tvInforAlbum.setText(String.format("%s  |  %s", albumArtist, trackAdapter.convertToDate(countDuration())));
        tvRandomList = findViewById(R.id.tv_random_list);
        tvRandomList.setOnClickListener(this);

        lvListSong = findViewById(R.id.lv_detail_al_item_song);
        lvListSong.setAdapter(trackAdapter);

        //Bottom Menu
        viewBottom = findViewById(R.id.bottom_menu);
        llDetailTitleSong = findViewById(R.id.ll_detail_title_song);
        llDetailTitleSong.setOnClickListener(this);
        imvSong = findViewById(R.id.imv_image_song);
        imvSong.setOnClickListener(this);
        imvNext = findViewById(R.id.imv_next);
        imvPrevious = findViewById(R.id.imv_previous);
        imvPausePlay = findViewById(R.id.imv_pause_play);
        imvNext.setOnClickListener(this);
        imvPrevious.setOnClickListener(this);
        imvPausePlay.setOnClickListener(this);
        tvTitleSong = findViewById(R.id.tv_bottom_title_song);
        tvTitleSong.setSelected(true);
        tvNameArtist = findViewById(R.id.tv_bottom_name_artist);
        isFirstRun = false;
        showBottomLayout(isFirstRun);
    }


    private void showBottomLayout(boolean isFirstRun) {
        if (isFirstRun) {
            viewBottom.setVisibility(View.VISIBLE);
        } else {
            viewBottom.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_back:
                finish();
                break;
            case R.id.tv_action_search:
                actionSearch();
                break;
            case R.id.tv_action_setting:
                //actionSetting();
                Snackbar.make(v, "Please input Setting", Snackbar.LENGTH_LONG).show();
                break;
            case R.id.tv_random_list:
                actionPlayRandomList();
                break;
            case R.id.imv_image_song:
                actionShowDetailSong();
                break;
            case R.id.ll_detail_title_song:
                actionShowDetailSong();
                break;
            case R.id.imv_next:
                break;
            case R.id.imv_previous:
                break;
            case R.id.imv_pause_play:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Const.REQUEST_CODE_ACTION_SEARCH_DETAIL_ALBUM:
                if (resultCode == RESULT_OK) {
                    //TODO play music
                    //setinfor
                    if (!isFirstRun) {
                        showBottomLayout(true);
                        isFirstRun = true;
                    }
                }
                break;
        }
    }

    private void actionSearch() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivityForResult(intent, Const.REQUEST_CODE_ACTION_SEARCH_DETAIL_ALBUM);
    }

    private void actionShowDetailSong() {
        Intent intent = new Intent(this, DetailSongActivity.class);
        startActivity(intent);
    }

    private void actionPlayRandomList() {
        SharePreferencesController.getInstance(this).putBoolean(Const.MEDIA_SHUFFLE, Const.MEDIA_SHUFFLE_TRUE);
        mediaManager.setArrItemSong(arrItemTrack);
        mediaManager.setCurrentIndex(new Random().nextInt(arrItemTrack.size() - 1));
        mediaManager.play(true);
        Intent intent = new Intent(this, DetailSongActivity.class);
        startActivity(intent);
    }

    public void getData() {
        Intent intent = getIntent();
        albumID = intent.getIntExtra(Const.KEY_ALBUM_ID, -1);
        albumName = intent.getStringExtra(Const.KEY_ALBUM_NAME);
        albumArtist = intent.getStringExtra(Const.KEY_ALBUM_ARTIST);
        trackAdapter = new TrackAdapter(this, playMusic);
        arrItemTrack = MediaManager.getInstance(this)
                .getSongList(MediaStore.Audio.Media.ALBUM_ID + "=?", new String[]{albumID + ""});
        trackAdapter.setArrItemTrack(arrItemTrack);
    }

    public void setInforBottomLayout(String nameSong, String nameArtist) {
        tvTitleSong.setText(nameSong);
        tvNameArtist.setText(nameArtist);
    }

    private int countDuration() {
        int sum = 0;
        for (ItemSong item : arrItemTrack) {
            sum += item.getDuration();
        }
        return sum;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateInforBottomLayout();
            handler.postDelayed(this, 200);
        }
    };

    private void updateInforBottomLayout() {
        if (mediaManager.getmPlayer().isPlaying()) {
            imvPausePlay.setImageResource(R.drawable.ic_pause);
        } else {
            imvPausePlay.setImageResource(R.drawable.ic_play);
        }
    }

    OnPlayMusic playMusic = new OnPlayMusic() {
        @Override
        public void playSong(List<ItemSong> list, int position) {
            Toast.makeText(DetailAlbumActivity.this, "Play album size=:" + list.size() + " in position=" + position, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
