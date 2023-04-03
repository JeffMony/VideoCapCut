package com.jeffmony.testve;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jeffmony.media.LogTag;
import com.jeffmony.media.MediaSdk;
import com.jeffmony.media.VideoRenderListener;
import com.jeffmony.media.camera.Facing;
import com.jeffmony.media.camera.OnFocusCallback;
import com.jeffmony.media.effect.InputType;
import com.jeffmony.media.encode.EncodeFormat;
import com.jeffmony.media.export.ExportInfo;
import com.jeffmony.media.record.IVideoRecord;
import com.jeffmony.media.record.RecordListener;
import com.jeffmony.media.record.Resolution;
import com.jeffmony.media.record.TakePictureListener;
import com.jeffmony.media.util.ImageUtils;
import com.jeffmony.media.view.PreviewSurfaceView;

public class RecordActivity extends AppCompatActivity {

    private IVideoRecord mVideoRecord;
    private int mBackgroundId = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        mVideoRecord = MediaSdk.createVideoRecord(this.getApplicationContext());
        PreviewSurfaceView surfaceView = findViewById(R.id.surface_view);

        surfaceView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mVideoRecord.focus(new PointF(event.getX(), event.getY()), new OnFocusCallback() {
                    @Override
                    public void onFocus(boolean success) {
                        Log.i(LogTag.TAG, "onFocus success="+success);
                    }
                });
            }
            return false;
        });

        mVideoRecord.setSurfaceView(surfaceView);
        mVideoRecord.setCameraFacing(Facing.BACK);
        mVideoRecord.setPreviewResolution(Resolution.Resolution1920x1080);
        mVideoRecord.setPictureResolution(Resolution.Resolution3840x2160);
        mVideoRecord.setVideoRenderListener(new VideoRenderListener() {
            @Override
            public void onCreateEGLWindow() {
                super.onCreateEGLWindow();
                if (mBackgroundId == -1) {
                    mBackgroundId = mVideoRecord.addEffect("{\n" +
                            "    \"effect\":[\n" +
                            "        {\n" +
                            "            \"type\":\"background\",\n" +
                            "            \"backgroundType\":0,\n" +
                            "            \"blur\":10,\n" +
                            "            \"renderFrameType\":1,\n" +
                            "            \"z_order\":1\n" +
                            "        }\n" +
                            "    ]\n" +
                            "}", InputType.BUFFER);
                }
            }

            @Override
            public int onDrawFrame(int texture, int width, int height) {
                return super.onDrawFrame(texture, width, height);
            }
        });

        mVideoRecord.setRecordListener(new RecordListener() {
            @Override
            public void onError(int type, int code, String msg) {
                super.onError(type, code, msg);
                Log.e(LogTag.TAG, "record failed, ret="+code);
            }

            @Override
            public void onProgress(int duration) {
                super.onProgress(duration);
                Log.i(LogTag.TAG, "record progress="+duration);
            }

            @Override
            public void onComplete() {
                super.onComplete();
                Log.i(LogTag.TAG, "record complete");
            }
        });

        final Button recordBtn = findViewById(R.id.btn_record);
        recordBtn.setOnClickListener(v -> {
            if (mVideoRecord == null) {
                return;
            }
            if (mVideoRecord.isRecording()) {
                mVideoRecord.stopRecord();
                Toast.makeText(this, "录制成功", Toast.LENGTH_SHORT).show();
                recordBtn.setText("开始录制");
            } else {
                String name = "/sdcard/DCIM/Camera/jeffmony-record-" + System.currentTimeMillis() +".mp4";
                ExportInfo exportInfo = new ExportInfo(name);
                exportInfo.setWidth(1080);
                exportInfo.setHeight(1920);
                exportInfo.setVideoBitRate(10000);
                exportInfo.setEncodeFormat(EncodeFormat.H265);
                exportInfo.setMediaCodecEncode(true);
                int ret = mVideoRecord.startRecord(exportInfo);
                if (ret == 0) {
                    Toast.makeText(this, "正在录制", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "录制失败", Toast.LENGTH_SHORT).show();
                }
                recordBtn.setText("停止录制");
            }
        });

        findViewById(R.id.btn_capture).setOnClickListener(v -> {
            if (mVideoRecord == null) {
                return;
            }
            mVideoRecord.takePicture(new TakePictureListener() {
                @Override
                public void onTakePicture(Bitmap bitmap) {
                    super.onTakePicture(bitmap);
                    Log.i(LogTag.TAG, "w="+bitmap.getWidth()+", h="+bitmap.getHeight());
                    String path = "/sdcard/DCIM/Camera/jeffmony-photo-" + System.currentTimeMillis() + ".jpg";
                    ImageUtils.saveImage(bitmap, path);
                    bitmap.recycle();
                }

                @Override
                public void onError(int code) {
                    super.onError(code);
                }
            });
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoRecord != null) {
            mVideoRecord.startPreview();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoRecord != null) {
            mVideoRecord.stopPreview();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoRecord != null) {
            mVideoRecord.destroy();
        }
    }
}
