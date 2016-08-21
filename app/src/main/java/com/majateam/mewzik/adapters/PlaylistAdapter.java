package com.majateam.mewzik.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.majateam.mewzik.R;
import com.majateam.mewzik.models.BaseObject;
import com.majateam.mewzik.models.Playlist;
import com.majateam.mewzik.models.Track;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Nicolas Martino on 20/08/2016.
 */
public class PlaylistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //Playlists and Tracks
    private List<BaseObject> items;
    private Context mContext;

    private final int PLAYLIST = 0, TRACK = 1;

    public PlaylistAdapter(Context context, List<BaseObject> items) {
        this.items = items;
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Playlist) {
            return PLAYLIST;
        } else if (items.get(position) instanceof Track) {
            return TRACK;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            default:
            case PLAYLIST:
                View viewP = inflater.inflate(R.layout.item_playlist, viewGroup, false);
                viewHolder = new PlaylistViewHolder(viewP);
                break;
            case TRACK:
                View viewT = inflater.inflate(R.layout.item_track, viewGroup, false);
                viewHolder = new TrackViewHolder(viewT);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            default:
            case PLAYLIST:
                PlaylistViewHolder vhP = (PlaylistViewHolder) viewHolder;
                configurePlaylistViewHolder(vhP, position);
                break;
            case TRACK:
                TrackViewHolder vhT = (TrackViewHolder) viewHolder;
                configureTrackViewHolder(vhT, position);
                break;
        }
    }

    private void configurePlaylistViewHolder(PlaylistViewHolder vhP, int position) {
        Playlist playlist = (Playlist) items.get(position);
        if (playlist != null) {
            vhP.getPlayListTitle().setText(playlist.getTitle());
        }
    }

    private void configureTrackViewHolder(TrackViewHolder vhT, int position) {
        Track track = (Track) items.get(position);
        if (track != null) {
            Picasso.with(mContext)
                    .load(track.getArtwork_url())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_dialog_alert)
                    .into(vhT.getTrackImage());
            vhT.getTrackText().setText(track.getTitle());
        }
    }

    public class PlaylistViewHolder extends RecyclerView.ViewHolder {

        private TextView playListTitle;

        public PlaylistViewHolder(View v) {
            super(v);
            playListTitle = (TextView) v.findViewById(R.id.playlist_title);
        }

        public TextView getPlayListTitle() {
            return playListTitle;
        }

        public void setPlayListTitle(TextView playListTitle) {
            this.playListTitle = playListTitle;
        }
    }

    public class TrackViewHolder extends RecyclerView.ViewHolder {

        private ImageView trackImage;
        private TextView trackText;

        public TrackViewHolder(View v) {
            super(v);
            trackImage = (ImageView) v.findViewById(R.id.track_image);
            trackText = (TextView) v.findViewById(R.id.track_title);
        }

        public ImageView getTrackImage() {
            return trackImage;
        }

        public void setTrackImage(ImageView trackImage) {
            this.trackImage = trackImage;
        }

        public TextView getTrackText() {
            return trackText;
        }

        public void setTrackText(TextView trackText) {
            this.trackText = trackText;
        }
    }
}
