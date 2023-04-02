package com.jeffmony.testve;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import com.github.florent37.runtimepermission.RuntimePermission;
import com.jeffmony.media.MediaSdk;
import com.jeffmony.media.effect.InputType;
import com.jeffmony.media.export.ExportInfo;
import com.jeffmony.media.export.ExportListener;
import com.jeffmony.media.export.IVideoExport;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RuntimePermission.askPermission(this)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO)
                .onAccepted((result -> {
                    /**
                     * 权限已经申请成功
                     */
                })).ask();

        findViewById(R.id.btn_editor).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditorActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_audio).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AudioActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_record).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RecordActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_video_info).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, VideoInfoActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_video_export).setOnClickListener(v -> {
            IVideoExport videoExport = MediaSdk.createVideoExport();
            String config = "/sdcard/poizon/config.json";
            ExportInfo exportInfo = new ExportInfo("/sdcard/poizon/output.mp4");
            videoExport.export(config, InputType.FILE, exportInfo, new ExportListener() {
                @Override
                public void onExportProgress(float progress) {
                    super.onExportProgress(progress);
                    android.util.Log.e("litianpeng", "onExportProgress progress="+progress);
                }

                @Override
                public void onExportComplete() {
                    super.onExportComplete();
                    android.util.Log.e("litianpeng", "onExportComplete");
                }

                @Override
                public void onExportFailed(int type, int code, String msg) {
                    super.onExportFailed(type, code, msg);
                    android.util.Log.e("litianpeng", "onExportFailed code="+code);
                }

                @Override
                public void onExportCancel() {
                    super.onExportCancel();
                }
            });
        });
    }


}