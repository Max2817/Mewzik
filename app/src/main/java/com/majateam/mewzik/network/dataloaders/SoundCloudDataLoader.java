package com.majateam.mewzik.network.dataloaders;

import android.util.Log;

import com.majateam.mewzik.config.MewzikConfig;
import com.majateam.mewzik.models.Playlist;
import com.majateam.mewzik.network.ServiceGenerator;
import com.majateam.mewzik.network.services.SoundCloudService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nicolas Martino on 20/08/2016.
 */
public class SoundCloudDataLoader {

    private static final String TAG = SoundCloudDataLoader.class.getSimpleName();
    private SoundCloudDataLoaderListener mListener;

    public SoundCloudDataLoader(SoundCloudDataLoaderListener listener){
        mListener = listener;
    }

    /**
     * Load the playlists from SoundCloud if we would save it we could you Realm
     */
    public void getPlaylist(String search){
        SoundCloudService soundCloudService = ServiceGenerator.createService(SoundCloudService.class);
        Call<ArrayList<Playlist>> call = soundCloudService.requestPlaylist(MewzikConfig.CLIENT_ID, search, 3);
        call.enqueue(new Callback<ArrayList<Playlist>>() {
            @Override
            public void onResponse(Call<ArrayList<Playlist>> call, Response<ArrayList<Playlist>> response) {
                if (response.isSuccessful()) {
                    List<Playlist> playlistList = response.body();
                    Log.v(TAG, "");
                    mListener.onSuccessGetPlaylist(new ArrayList<>(playlistList));
                } else {
                    mListener.onFailGetPlaylist(response.message());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Playlist>> call, Throwable t) {
                // handle error
                Log.v(TAG, call.request().url() + " failed because: " + t.getMessage());
                mListener.onFailGetPlaylist(t.getMessage());
            }
        });
    }

    public interface SoundCloudDataLoaderListener {
        void onSuccessGetPlaylist(ArrayList<Playlist> playlist);
        void onFailGetPlaylist(String errorMessage);
    }
}
