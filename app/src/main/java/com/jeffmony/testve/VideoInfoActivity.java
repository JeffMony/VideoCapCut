package com.jeffmony.testve;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jeffmony.media.MediaSdk;
import com.jeffmony.media.video.IVideoExtract;

public class VideoInfoActivity extends AppCompatActivity {

    private IVideoExtract mVideoExtract;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_info);
        mVideoExtract = MediaSdk.createVideoExtract();

        int ret = mVideoExtract.setPath("/sdcard/DCIM/Camera/output.mp4");
        android.util.Log.e("litianpeng", "setPath ret="+ret);

        int duration = mVideoExtract.getDuration();
        android.util.Log.e("litianpeng", "duration="+duration);

        int width = mVideoExtract.getWidth();
        int height = mVideoExtract.getHeight();
        android.util.Log.e("litianpeng", "width="+width+", height="+height);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoExtract != null) {
            mVideoExtract.destroy();
        }
    }
}
