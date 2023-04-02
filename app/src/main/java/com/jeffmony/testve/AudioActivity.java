package com.jeffmony.testve;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jeffmony.media.LogTag;
import com.jeffmony.media.MediaSdk;
import com.jeffmony.media.audio.AudioInfoListener;
import com.jeffmony.media.audio.AudioListener;
import com.jeffmony.media.audio.IAudioInfo;
import com.jeffmony.media.audio.IAudioPlayer;

import java.util.List;

public class AudioActivity extends AppCompatActivity {

    private IAudioPlayer mAudioPlayer;
    private IAudioInfo mAudioInfo;
    private SeekBar mSeekBar;
    private int mDuration = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        mSeekBar = findViewById(R.id.seek_bar);

        mAudioPlayer = MediaSdk.createAudioPlayer();
        mAudioPlayer.setLoop(true);
        mAudioPlayer.prepare("/sdcard/qqmusic/song/和田薫 (わだかおる) - 時代を越える想い [mqms2].mp3");
        mAudioPlayer.setAudioListener(new AudioListener() {

            @Override
            public void onPrepared() {
                super.onPrepared();
                Log.e(LogTag.TAG, "onPrepared");
            }

            @Override
            public void onError(int type, int code, String msg) {
                super.onError(type, code, msg);
                Log.e(LogTag.TAG, "code="+code);
            }

            @Override
            public void onPosition(int position, int duration) {
                super.onPosition(position, duration);
                Log.e(LogTag.TAG, "position="+position+", duration="+duration);
                if (duration != mDuration) {
                    mDuration = duration;
                    mSeekBar.setMax(duration);
                }
                mSeekBar.setProgress(position);
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mAudioPlayer.seek(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (mAudioPlayer != null) {
                    mAudioPlayer.pause();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mAudioPlayer != null) {
                    mAudioPlayer.start();
                }
            }
        });

        final Button mPlayPauseBtn = findViewById(R.id.btn_play_pause);
        mPlayPauseBtn.setOnClickListener(v -> {
            if (mAudioPlayer == null) {
                return;
            }
            if (mAudioPlayer.isPlaying()) {
                mAudioPlayer.pause();
                mPlayPauseBtn.setText("播放");
            } else {
                mAudioPlayer.start();
                mPlayPauseBtn.setText("暂停");
            }
        });
        findViewById(R.id.btn_next).setOnClickListener(v -> {
            if (mAudioPlayer == null) {
                return;
            }
            mAudioPlayer.stop();
            mAudioPlayer.prepare("/sdcard/DCIM/Camera/zhenzhong.aac");
            mAudioPlayer.start();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAudioPlayer != null) {
            mAudioPlayer.pause();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAudioInfo != null) {
            mAudioInfo.destroy();
        }
        if (mAudioPlayer != null) {
            mAudioPlayer.release();
        }
    }
}
