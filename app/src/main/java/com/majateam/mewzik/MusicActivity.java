package com.majateam.mewzik;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.majateam.mewzik.adapters.MusicAdapter;
import com.majateam.mewzik.listeners.ItemClickListener;
import com.majateam.mewzik.utils.ResizableRecyclerViewLinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Display music brought back from Facebook
 * Created by Nicolas Martino on 20/08/2016.
 */
public class MusicActivity extends AppCompatActivity implements ItemClickListener{

    public static final String USER_ID = "user_id";
    private String mUserId;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.progress_text)
    TextView mProgressText;
    @BindView(R.id.music_list)
    RecyclerView mMusicRecyclerView;
    private LoadAllMusicLikesTask mLoadLikesTask;
    private ArrayList<String> mMusicList;
    private MusicAdapter mMusicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        mMusicList = new ArrayList<>();
        if (extras != null) {
            mUserId = extras.getString(USER_ID);

        }
        if(mUserId != null){
            mLoadLikesTask = new LoadAllMusicLikesTask();
            mLoadLikesTask.execute();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public void onItemSelected(View v, int position) {
        Intent intent = new Intent(MusicActivity.this, PlaylistActivity.class);
        intent.putExtra(PlaylistActivity.MUSIC, mMusicList.get(position));
        startActivity(intent);
    }

    //Using this model but I would have used the recyclerview scolling to load and display more data
    class LoadAllMusicLikesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setIndeterminate(true);
            mProgressText.setText(R.string.loading_retrieving_facebook_music);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            final String[] afterString = {""};  // will contain the next page cursor
            final Boolean[] noData = {false};   // stop when there is no after cursor
            do {
                Bundle params = new Bundle();
                params.putString("after", afterString[0]);
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        mUserId + "/likes",
                        params,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse graphResponse) {
                                JSONObject jsonObject = graphResponse.getJSONObject();
                                try {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                                    //  your code
                                    if (jsonArray != null) {
                                        for (int i=0; i<jsonArray.length(); i++){
                                            JSONObject musicObject = jsonArray.getJSONObject(i);
                                            if(musicObject != null){
                                                mMusicList.add(musicObject.getString("name"));
                                            }
                                        }
                                    }

                                    if(!jsonObject.isNull("paging")) {
                                        JSONObject paging = jsonObject.getJSONObject("paging");
                                        JSONObject cursors = paging.getJSONObject("cursors");
                                        if (!cursors.isNull("after"))
                                            afterString[0] = cursors.getString("after");
                                        else
                                            noData[0] = true;
                                    }
                                    else
                                        noData[0] = true;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                ).executeAndWait();
            }
            while(!noData[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);
            mProgressBar.setVisibility(View.GONE);
            mProgressText.setVisibility(View.GONE);
            refreshData();
        }

    }

    private void refreshData(){
        mMusicAdapter = new MusicAdapter(mMusicList);
        mMusicAdapter.setMusicSelectionListener(this);
        mMusicRecyclerView.setLayoutManager(new ResizableRecyclerViewLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mMusicRecyclerView.setAdapter(mMusicAdapter);
        mMusicRecyclerView.setVisibility(View.VISIBLE);
        mMusicAdapter.notifyDataSetChanged();
    }
}
