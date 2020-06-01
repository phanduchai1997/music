package com.t3h.dev.music.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.t3h.dev.music.R;
import com.t3h.dev.music.item.ItemSong;
import com.t3h.dev.music.listener.OnPlayMusic;

/**
 * Created by sev_user on 12/20/2016.
 */

public class TrackAdapter extends BaseAdapter {
    private ArrayList<ItemSong> arrItemTrack = new ArrayList<>();
    private LayoutInflater inflater;
    private OnPlayMusic onPlayMusic;

    public TrackAdapter(Context context, OnPlayMusic playMusic) {
        inflater = LayoutInflater.from(context);
        onPlayMusic = playMusic;
    }

    @Override
    public int getCount() {
        return arrItemTrack.size();
    }

    @Override
    public ItemSong getItem(int position) {
        return arrItemTrack.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        Holder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_tracks, null);
            holder = new Holder();
            holder.tvTracksName = view.findViewById(R.id.tv_title_tracks);
            holder.tvDuration = view.findViewById(R.id.tv_duration);
            holder.imvAnim = view.findViewById(R.id.imv_animation_tracks);
            holder.llMain = view.findViewById(R.id.ll_main_detail_album);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        ItemSong item = arrItemTrack.get(position);

        String nameSong = item.getDisplayName();
        if (nameSong.indexOf("-") > 0) {
            nameSong = nameSong.substring(0, nameSong.indexOf("-"));
        } else if (nameSong.indexOf("_") > 0) {
            nameSong = nameSong.substring(0, nameSong.indexOf("_"));
        }
        holder.tvTracksName.setText(nameSong);
        holder.tvDuration.setText(convertToDate(item.getDuration()));
        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlayMusic.playSong(arrItemTrack, position);
            }
        });
        return view;
    }

    private class Holder {
        ImageView imvAnim;
        TextView tvTracksName;
        TextView tvDuration;
        LinearLayout llMain;
    }

    public String convertToDate(Integer value) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        return dateFormat.format(new Date(value));
    }

    public void setArrItemTrack(ArrayList<ItemSong> arrItemTrack) {
        this.arrItemTrack = arrItemTrack;
    }
}
