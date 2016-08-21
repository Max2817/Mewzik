package com.majateam.mewzik.models;

/**
 * Created by Nicolas Martino on 20/08/2016.
 */
public class Track extends BaseObject{

    private String kind;
    private String title;
    private String artwork_url;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtwork_url() {
        return artwork_url;
    }

    public void setArtwork_url(String artwork_url) {
        this.artwork_url = artwork_url;
    }
}
