package com.majateam.mewzik;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.majateam.mewzik.adapters.PlaylistAdapter;
import com.majateam.mewzik.models.BaseObject;
import com.majateam.mewzik.models.Playlist;
import com.majateam.mewzik.models.Track;
import com.majateam.mewzik.network.dataloaders.SoundCloudDataLoader;
import com.majateam.mewzik.utils.ResizableRecyclerViewLinearLayoutManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Playlist screen with the tracks
 * Created by Nicolas Martino on 20/08/2016.
 */
public class PlaylistActivity extends AppCompatActivity implements SoundCloudDataLoader.SoundCloudDataLoaderListener {

    public static final String MUSIC = "music";
    public static final String TRACK = "track";
    private String mMusic;
    private ArrayList<BaseObject> mList;
    private PlaylistAdapter mPlayListAdapter;
    @BindView(R.id.playlist_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.progress_text)
    TextView mProgressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mMusic = extras.getString(MUSIC);
        }
        mList = new ArrayList<>();
        // Create an ArrayAdapter using the Array List
        mPlayListAdapter = new PlaylistAdapter(this, mList);

        LinearLayoutManager mLayoutManager = new ResizableRecyclerViewLinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mPlayListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setIndeterminate(true);
        mProgressText.setVisibility(View.VISIBLE);
        mProgressText.setText(R.string.loading_retrieving_soundcloud_playlist);
        SoundCloudDataLoader soundCloudDataLoader = new SoundCloudDataLoader(this);
        soundCloudDataLoader.getPlaylist(mMusic);

    }

    @Override
    public void onSuccessGetPlaylist(ArrayList<Playlist> playlist) {
        mList.clear();
        for(Playlist play : playlist){
            mList.add(play);
            for(Track track : play.getTracks()){
                if(track.getKind().equals(TRACK)) {
                    mList.add(track);
                }
            }
        }
        mProgressBar.setVisibility(View.GONE);
        mProgressText.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mPlayListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailGetPlaylist(String errorMessage) {

    }
}
