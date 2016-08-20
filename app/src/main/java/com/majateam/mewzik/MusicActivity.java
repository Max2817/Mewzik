package com.majateam.mewzik;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MusicActivity extends AppCompatActivity {

    public static final String USER_ID = "USER_ID";
    private String mUserId;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    private LoadAllMusicLikesTask mLoadLikesTask;
    private ArrayList<String> mMusicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mUserId = extras.getString(USER_ID);
            mLoadLikesTask = new LoadAllMusicLikesTask();
            mLoadLikesTask.execute();
        }

    }


    class LoadAllMusicLikesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setIndeterminate(true);
            mMusicList = new ArrayList<>();
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
        }

    }
}
