package com.example.locationmusic;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
ImageView nextIv,lastIv,playIv;
RecyclerView musicRv;
List<localmusicBean> mDatas;
    private LocalMusicAdapter adapter;
    private int currentPlayPosition=-1;
    private TextView singerTv,songTv;
   private   MediaPlayer mediaPlayer;
   int currentPauseSong = 0;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        mediaPlayer = new MediaPlayer();
        initView();
        LoadLocalMusicData();
setEventListener();
    }

    private void setEventListener() {
        adapter.setOnClickListener(new LocalMusicAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                currentPlayPosition = position;
                localmusicBean bean = mDatas.get(position);
                playMusicPosition(bean);
            }
        });

    }

    public void playMusicPosition(localmusicBean bean) {
        singerTv.setText(bean.getSinger());
        songTv.setText(bean.getSong());
        stopMusic();
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(bean.getPath());
            playMusic();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMusic();
    }

    private void initView() {
//        初始化控件函数
        nextIv = findViewById(R.id.local_music_bottom_IV_next);
        lastIv = findViewById(R.id.local_music_bottom_iv_last);
        playIv = findViewById(R.id.local_music_bottom_iv_play);
        musicRv = findViewById(R.id.local_music_Rv);
        songTv = findViewById(R.id.local_music_bottom_Tv_song);
        singerTv = findViewById(R.id.local_music_bottom_Tv_singer);
        playIv.setOnClickListener(this);
        nextIv.setOnClickListener(this);
        lastIv.setOnClickListener(this);
        mDatas = new ArrayList<>();
        adapter = new LocalMusicAdapter(this,mDatas);
        musicRv.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        musicRv.setLayoutManager(manager);



    }

    private void LoadLocalMusicData() {
        ContentResolver resolver = getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            Cursor query = resolver.query(uri,null,null,null,null);
            int id = 0;
            while (query.moveToNext()) {
                String song = query.getString(query.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String singer = query.getString(query.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String album = query.getString(query.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                id++;
                String s = String.valueOf(id);
                String path = query.getString(query.getColumnIndex(MediaStore.Audio.Media.DATA));
                long duration =  query.getLong(query.getColumnIndex(MediaStore.Audio.Media.DURATION));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                String time = simpleDateFormat.format(new Date(duration));
                localmusicBean bean = new localmusicBean(s, song, singer, album, time, path);
                mDatas.add(bean);
            adapter.notifyDataSetChanged();

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.local_music_bottom_iv_last:
                if (currentPlayPosition==0) {
                    Toast.makeText(this,"没有上一曲了，已经是第一曲了！",Toast.LENGTH_SHORT).show();
                    return;
                }
                currentPlayPosition = currentPlayPosition-1;
                localmusicBean lastBean = mDatas.get(currentPlayPosition);
                playMusicPosition(lastBean);
                break;
            case R.id.local_music_bottom_IV_next:
                if (currentPlayPosition==mDatas.size()-1) {
                    Toast.makeText(this,"没有上一曲了，已经是第一曲了！",Toast.LENGTH_SHORT).show();
                    return;
                }
                currentPlayPosition = currentPlayPosition+1;
                localmusicBean nextBean = mDatas.get(currentPlayPosition);
                playMusicPosition(nextBean);
                break;
            case R.id.local_music_bottom_iv_play:
                if (currentPlayPosition==-1) {
                    Toast.makeText(this,"请选择要播放的音乐",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mediaPlayer.isPlaying()) {
                    pauseMusic();
                }else {
                    playMusic();

                }
        }
    }

    private void pauseMusic() {
        if (mediaPlayer!=null&&mediaPlayer.isPlaying()){
            currentPauseSong = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
            playIv.setImageResource(R.mipmap.start1);

        }
    }

    private void stopMusic() {
        if (mediaPlayer!=null) {
            currentPauseSong=0;
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            mediaPlayer.stop();
            playIv.setImageResource(R.mipmap.start1);
        }
    }
    private void playMusic() {
        if (mediaPlayer!=null&&!mediaPlayer.isPlaying()) {
            if (currentPauseSong==0) {
            try {
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            }else {
mediaPlayer.seekTo(currentPauseSong);
mediaPlayer.start();
            }
            playIv.setImageResource(R.mipmap.pause);
        }
    }

}
