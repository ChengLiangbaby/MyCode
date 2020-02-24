package com.example.locationmusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LocalMusicAdapter extends RecyclerView.Adapter<LocalMusicAdapter.LocalMusicViewHolder> {
    Context context;
    OnItemClickListener listener;

    public void setOnClickListener(OnItemClickListener onClickListener) {
        listener = onClickListener;
    }

    List<localmusicBean> mDatas;
public interface OnItemClickListener{
    public void OnItemClick(View view,int position);
}

    public LocalMusicAdapter(Context context, List<localmusicBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @NonNull
    @Override
    public  LocalMusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_localmusic,parent,false);
        LocalMusicViewHolder holder = new LocalMusicViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LocalMusicViewHolder holder, final int position) {
        localmusicBean bean = mDatas.get(position);
        holder.idTv.setText(bean.getId());
        holder.albumTv.setText(bean.getAlbum());
        holder.singerTv.setText(bean.getSinger());
        holder.songTv.setText(bean.getSong());
        holder.timeTv.setText(bean.getDuration());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnItemClick(v,position);
            }
        });
    }



    @Override
    public int getItemCount() {
        return mDatas.size();
    }
    class LocalMusicViewHolder extends RecyclerView.ViewHolder{
        TextView idTv,songTv,singerTv,albumTv,timeTv;
        public LocalMusicViewHolder(@NonNull View itemView) {
            super(itemView);
            idTv = itemView.findViewById(R.id.item_local_music_num);
            songTv = itemView.findViewById(R.id.item_local_music_song);
            singerTv = itemView.findViewById(R.id.item_local_music_singer);
            albumTv = itemView.findViewById(R.id.item_local_music_album);
            timeTv = itemView.findViewById(R.id.item_local_music_duration);

        }
    }
}
