package com.majateam.mewzik.network.services;

import com.majateam.mewzik.models.Playlist;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Nicolas Martino on 20/08/2016.
 */
public interface SoundCloudService {
    @GET("playlists/")
    Call<ArrayList<Playlist>> requestPlaylist(@Query("client_id") String clientId, @Query("q") String search, @Query("limit") int limit);
}
