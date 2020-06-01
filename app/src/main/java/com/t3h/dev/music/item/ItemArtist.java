package com.t3h.dev.music.item;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by sev_user on 12/19/2016.
 */

public class ItemArtist {
    private int idArtist;
    private String artistName;
    private int numOfAlbums;
    private int numOfTracks;

    public ItemArtist(int idArtist, String artistName, int numOfAlbums, int numofTracks) {
        this.idArtist = idArtist;
        this.artistName = artistName;
        this.numOfAlbums = numOfAlbums;
        this.numOfTracks = numofTracks;
    }

    public Bitmap getImageSong(Context context) {
        Uri sArtworkUri = Uri
                .parse("content://media/external/audio/albumart");
        Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, Long.parseLong(idArtist + ""));

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(
                    context.getContentResolver(), albumArtUri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public int getIdArtist() {
        return idArtist;
    }

    public void setIdArtist(int idArtist) {
        this.idArtist = idArtist;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getNumOfAlbums() {
        return numOfAlbums;
    }

    public void setNumOfAlbums(int numOfAlbums) {
        this.numOfAlbums = numOfAlbums;
    }

    public int getNumofTracks() {
        return numOfTracks;
    }

    public void setNumofTracks(int numofTracks) {
        this.numOfTracks = numofTracks;
    }
}
