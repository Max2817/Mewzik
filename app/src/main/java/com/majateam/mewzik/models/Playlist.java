package com.majateam.mewzik.models;

import java.util.ArrayList;

/**
 * Created by Nicolas Martino on 20/08/2016.
 */
public class Playlist extends BaseObject {

    private String title;
    private ArrayList<Track> tracks;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Track> getTracks() {
        return tracks;
    }

    public void setTracks(ArrayList<Track> tracks) {
        this.tracks = tracks;
    }
}
