package com.t3h.dev.music.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.t3h.dev.music.R;
import com.t3h.dev.music.adapter.PagerAdapter;
import com.t3h.dev.music.common.Const;
import com.t3h.dev.music.item.ItemSong;
import com.t3h.dev.music.media.MediaManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    //Bottom Menu
    private View viewBottom;
    private ImageView imvPausePlay;
    private ImageView imvPrevious, imvNext;
    private ImageView imvImageSong;
    private TextView tvTitleSong, tvNameArtist;
    private LinearLayout llDetailTitleSong;
    private DrawerLayout drawer;
    private ViewPager viewPager;
    private MediaManager mediaManager;
    private Handler handler = new Handler();
    private NavigationView navigationView;
    private ImageView imvMenu, imvSetting, imvSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setOnClick();
        mediaManager = MediaManager.getInstance(this);
        MainActivity.this.runOnUiThread(runnable);
        showBottomLayout(false);
    }

    private void setOnClick() {
        navigationView.setNavigationItemSelectedListener(this);
        imvMenu.setOnClickListener(this);
        imvSetting.setOnClickListener(this);
        imvSearch.setOnClickListener(this);
        llDetailTitleSong.setOnClickListener(this);
        imvImageSong.setOnClickListener(this);
        imvNext.setOnClickListener(this);
        imvPrevious.setOnClickListener(this);
        imvPausePlay.setOnClickListener(this);
    }

    private void initView() {
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        imvMenu = findViewById(R.id.imv_menu_drawer);
        imvSetting = findViewById(R.id.imv_setting);
        imvSearch = findViewById(R.id.imv_search);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), 3);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //Bottom Menu
        viewBottom = findViewById(R.id.bottom_menu);
        llDetailTitleSong = findViewById(R.id.ll_detail_title_song);
        imvImageSong = findViewById(R.id.imv_image_song);
        imvNext = findViewById(R.id.imv_next);
        imvPrevious = findViewById(R.id.imv_previous);
        imvPausePlay = findViewById(R.id.imv_pause_play);
        tvTitleSong = findViewById(R.id.tv_bottom_title_song);
        tvTitleSong.setSelected(true);
        tvNameArtist = findViewById(R.id.tv_bottom_name_artist);
        if (mediaManager == null) {
            mediaManager = MediaManager.getInstance(this);
        }
        if (mediaManager.getArrItemSong().size() > 0) {
            ItemSong song = mediaManager.getArrItemSong().get(mediaManager.getCurrentIndex());
            setInforBottomLayout(song.getDisplayName(), song.getArtist());
        }
    }

    public void showBottomLayout(boolean isShow) {
        if (isShow) {
            viewBottom.setVisibility(View.VISIBLE);
        } else {
            viewBottom.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_song) {
            viewPager.setCurrentItem(0);
        } else if (id == R.id.nav_albums) {
            viewPager.setCurrentItem(1);
        } else if (id == R.id.nav_artist) {
            viewPager.setCurrentItem(2);
        } else if (id == R.id.mini_game) {
            Toast.makeText(this, "Click Mini game", Toast.LENGTH_SHORT).show();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_menu_drawer:
                drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.imv_setting:
                showPopupMenu(v);
                break;
            case R.id.imv_search:
                actionSearch();
                break;
            case R.id.imv_next:
                break;
            case R.id.imv_previous:
                break;
            case R.id.imv_pause_play:
                break;
            case R.id.imv_image_song:
                actionShowDetailSong(v);
                break;
            case R.id.ll_detail_title_song:
                actionShowDetailSong(v);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Const.REQUEST_CODE_ACTION_SEARCH_MAIN:
                if (resultCode == RESULT_OK) {
                    int position = getItemSong(data.getStringExtra(Const.KEY_ACTION_SEARCH_SONG_NAME));
                    mediaManager.setCurrentIndex(position);
                    mediaManager.play(true);
                    showBottomLayout(true);
                    setInforBottomLayout(mediaManager.getArrItemSong().get(position).getDisplayName(), mediaManager.getArrItemSong().get(position).getArtist());
                }
                break;
        }
    }

    private int getItemSong(String songName) {
        ArrayList<ItemSong> arr = mediaManager.getArrItemSong();
        int position = 0;
        for (ItemSong song : arr) {
            if (song.getDisplayName().equalsIgnoreCase(songName)) {
                return position;
            }
            position++;
        }
        return -1;
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

    private void actionSearch() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivityForResult(intent, Const.REQUEST_CODE_ACTION_SEARCH_MAIN);
    }


    private void actionShowDetailSong(View view) {
        Intent intent = new Intent(this, DetailSongActivity.class);
        startActivity(intent);
    }

    private void showPopupMenu(final View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.main, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_settings:
                        Snackbar.make(v, "Please input setting", Snackbar.LENGTH_LONG).show();
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    public void setInforBottomLayout(String nameSong, String nameArtist) {
        tvTitleSong.setText(nameSong);
        tvNameArtist.setText(nameArtist);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
