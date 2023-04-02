package com.jeffmony.testve;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jeffmony.media.MediaSdk;
import com.jeffmony.media.effect.InputType;
import com.jeffmony.media.encode.EncodeFormat;
import com.jeffmony.media.export.ExportInfo;
import com.jeffmony.media.export.ExportListener;
import com.jeffmony.media.export.IVideoExport;

public class ExportActivity extends AppCompatActivity {

    private final static String EXPORT_PATH = "/sdcard/DCIM/Camera/output.mp4";
    private final static int EXPORT_WIDTH = 960;
    private final static int EXPORT_HEIGHT = 540;
    private final Handler mMainHandler = new Handler(Looper.getMainLooper());

    private VideoView mVideoView;
    private TextView mResultView;
    private IVideoExport mVideoExport;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        mResultView = findViewById(R.id.text_export);
        mVideoView = findViewById(R.id.video_view);

        String draftPath = getIntent().getStringExtra("draftPath");
        mVideoExport = MediaSdk.createVideoExport();
        ExportInfo exportInfo = new ExportInfo(EXPORT_PATH);
        exportInfo.setFrameRate(30);
        exportInfo.setWidth(EXPORT_WIDTH);
        exportInfo.setHeight(EXPORT_HEIGHT);
        exportInfo.setEncodeFormat(EncodeFormat.H265);
        exportInfo.setMediaCodecEncode(true);
        exportInfo.setMediaCodecDecode(true);
        exportInfo.setVideoBitRate(10000);
        exportInfo.setParallelCount(2);
        exportInfo.setSupportParallel(false);
        int ret = mVideoExport.export(draftPath, InputType.FILE, exportInfo, new ExportListener() {
            @Override
            public void onExportCancel() {
                super.onExportCancel();
            }

            @Override
            public void onExportFailed(int type, int code, String msg) {
                super.onExportFailed(type, code, msg);
                mMainHandler.post(() -> {
                    mResultView.setText("导出失败");
                });
            }

            @Override
            public void onExportProgress(float progress) {
                super.onExportProgress(progress);
                mMainHandler.post(() -> {
                    mResultView.setText("导出进度 : " + (int)(progress * 100));
                });
            }

            @Override
            public void onExportComplete() {
                super.onExportComplete();
                mMainHandler.post(() -> {
                    mResultView.setText("导出成功");
                    mVideoView.stopPlayback();
                    mVideoView.setVideoPath(EXPORT_PATH);
                    mVideoView.start();
                });
            }
        });
        if (ret != 0) {
            Toast.makeText(this, "导出失败, ret="+ret, Toast.LENGTH_SHORT).show();
        }
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        ViewGroup.LayoutParams params = mVideoView.getLayoutParams();
        params.width = screenWidth;
        params.height = screenWidth * EXPORT_HEIGHT / EXPORT_WIDTH;
        mVideoView.setLayoutParams(params);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoExport != null) {
            mVideoExport.cancel();
        }
    }
}
