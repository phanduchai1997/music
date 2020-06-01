package com.t3h.dev.music.listener;

import java.util.List;

import com.t3h.dev.music.item.ItemSong;

public interface OnPlayMusic {
    void playSong(List<ItemSong> list, int position);
}
