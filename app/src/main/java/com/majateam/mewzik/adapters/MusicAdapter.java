package com.majateam.mewzik.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.majateam.mewzik.R;
import com.majateam.mewzik.listeners.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicolas Martino on 20/08/2016.
 */
public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ItemViewHolder> {

    private static final String TAG = MusicAdapter.class.getSimpleName();
    private List<String> mContentDataSource;
    private ItemClickListener mMusicSelectionListener;
    private Context mContext;


    public MusicAdapter(List<String> source) {
        mContentDataSource = new ArrayList<>();
        if (source != null)
            mContentDataSource = new ArrayList<>(source);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music, parent, false);
        return new ItemViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        int holderPosition = holder.getAdapterPosition();
        if (mContentDataSource != null && holderPosition < mContentDataSource.size()) {
            holder.position = holderPosition;
            holder.name.setText(mContentDataSource.get(holderPosition));
            holder.setClickListener(mMusicSelectionListener);
        }
    }

    @Override
    public int getItemCount() {
        return mContentDataSource.size();
    }

    public void setMusicSelectionListener(ItemClickListener listener) {
        this.mMusicSelectionListener = listener;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        public int position;
        public ItemClickListener clickListener;

        public ItemViewHolder(View rootView) {
            super(rootView);
            name = (TextView) rootView.findViewById(R.id.tv_item_name);
            rootView.setOnClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            this.clickListener.onItemSelected(v, getAdapterPosition());
        }
    }

}
