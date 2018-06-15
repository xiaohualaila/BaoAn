package com.hz.junxinbaoan.utils;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.SeekBar;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zangxl on 2017/3/30.
 */

public class AudioPlayerUtil implements MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener{
    public MediaPlayer mediaPlayer;
    private SeekBar skbProgress;
    private Timer mTimer=new Timer();
    private String urlPath;
    public AudioPlayerUtil(SeekBar skbProgress)
    {
        this.skbProgress=skbProgress;
        initMedioPlayer();
        mTimer.schedule(mTimerTask, 0, 500);
    }

    private void initMedioPlayer(){
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
        } catch (Exception e) {
            Log.e("mediaPlayer", "error", e);
        }
    }

    public AudioPlayerUtil(){
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        } catch (Exception e) {
            Log.e("mediaPlayer", "error", e);
        }
    }

    /*******************************************************
     * 通过定时器和Handler来更新进度条
     ******************************************************/
    TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            if(mediaPlayer==null)
                return;
            if (mediaPlayer.isPlaying() && skbProgress.isPressed() == false) {
                handleProgress.sendEmptyMessage(0);
            }
        }
    };

    Handler handleProgress = new Handler() {
        public void handleMessage(Message msg) {

            int position = mediaPlayer.getCurrentPosition();
            int duration = mediaPlayer.getDuration();

            if (duration > 0) {
                long pos = skbProgress.getMax() * position / duration;
                skbProgress.setProgress((int) pos);
            }
        };
    };
    //*****************************************************

    public void play()
    {
        if (mediaPlayer==null){
            initMedioPlayer();
            setUrl(urlPath);
        }
        mediaPlayer.start();
    }

    public void setUrl(String videoUrl)
    {
        this.urlPath=videoUrl;
        if (mediaPlayer != null){
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(videoUrl);
                mediaPlayer.prepare();//prepare之后自动播放
                //mediaPlayer.start();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }


    public void pause()
    {
        if (mediaPlayer != null)
            mediaPlayer.pause();
    }

    public void stop()
    {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    /**
      * 获取音乐播放时长
      * @return
      */
    public String getAudioTime(){//prepare之后才能获取到
        Log.e("mediaPlayer", "getAudioTime:"+mediaPlayer.getDuration());
        double time=((double) mediaPlayer.getDuration())/1000;
        BigDecimal b   =   new   BigDecimal(time);
        time   =   b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue();
        return time+"秒";
    }

    @Override
    /**
     * 通过onPrepared播放
     */
    public void onPrepared(MediaPlayer arg0) {
//        arg0.start();
        Log.e("mediaPlayer", "onPrepared");
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
        Log.e("mediaPlayer", "onCompletion");
        skbProgress.setProgress(skbProgress.getMax());
    }

    @Override
    public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
        if (skbProgress!=null){
            skbProgress.setSecondaryProgress(bufferingProgress);
            int currentProgress=skbProgress.getMax()*mediaPlayer.getCurrentPosition()/mediaPlayer.getDuration();
            Log.e(currentProgress+"% play", bufferingProgress + "% buffer");
        }
    }

}