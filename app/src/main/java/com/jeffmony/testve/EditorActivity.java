package com.jeffmony.testve;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jeffmony.media.LogTag;
import com.jeffmony.media.MediaSdk;
import com.jeffmony.media.VideoRenderListener;
import com.jeffmony.media.editor.AudioEffectListener;
import com.jeffmony.media.editor.ClipListener;
import com.jeffmony.media.editor.IVideoEditor;
import com.jeffmony.media.editor.IVideoThumbnailListener;
import com.jeffmony.media.editor.MediaClip;
import com.jeffmony.media.editor.VideoEditorListener;
import com.jeffmony.media.effect.EffectListener;
import com.jeffmony.media.effect.InputType;
import com.jeffmony.media.util.ImageUtils;
import com.jeffmony.media.view.PreviewSurfaceView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class EditorActivity extends AppCompatActivity {

    private SeekBar mVideoProgressBar;
    private SeekBar mFilterSeekBar;

    private IVideoEditor mVideoEditor;
    private int mBackgroundId = -1;
    private int mVideoDuration = -1;
    private int mMusicId = -1;
    private int mFilterId = -1;
    private String mDraftPath = "/sdcard/DCIM/Camera/draft.json";
    private int mAudioEffectId = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        mVideoProgressBar = findViewById(R.id.seek_video_progress);
        mFilterSeekBar = findViewById(R.id.seek_filter_progress);
        mVideoEditor = MediaSdk.createVideoEditor();
        mVideoEditor.setDraftPath(mDraftPath);
        PreviewSurfaceView surfaceView = findViewById(R.id.surface_view);
        mVideoEditor.setVideoRenderListener(new VideoRenderListener() {
            @Override
            public void onCreateEGLWindow() {
                super.onCreateEGLWindow();
                if (mBackgroundId == -1) {
                    mBackgroundId = mVideoEditor.addEffect("{\n" +
                            "    \"effect\":[\n" +
                            "        {\n" +
                            "            \"type\":\"background\",\n" +
                            "            \"backgroundType\":1,\n" +
                            "            \"red\":0,\n" +
                            "            \"green\":0,\n" +
                            "            \"blue\":0,\n" +
                            "            \"alpha\":0\n" +
                            "        }\n" +
                            "    ]\n" +
                            "}", InputType.BUFFER);
                }
                mVideoEditor.setLoop(false);
                mVideoEditor.prepare();
                mVideoEditor.play();
            }

            @Override
            public int onDrawFrame(int texture, int width, int height) {
                return super.onDrawFrame(texture, width, height);
            }
        });

        mVideoEditor.setVideoEditorListener(new VideoEditorListener() {
            @Override
            public void onComplete() {
                super.onComplete();
                Toast.makeText(EditorActivity.this, "播放完成", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPosition(int position, int duration) {
                super.onPosition(position, duration);
                if (duration != mVideoDuration) {
                    android.util.Log.e("litianpeng", "duration=" + duration);
                    mVideoDuration = duration;
                    mVideoProgressBar.setMax(duration);
                }
                mVideoProgressBar.setProgress(position);
            }

            @Override
            public void onError(int type, int code, String msg) {
                super.onError(type, code, msg);
            }
        });

        mVideoProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mVideoEditor.seek(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (mVideoEditor != null) {
                    mVideoEditor.pause();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mVideoEditor != null) {
                    mVideoEditor.seekComplete();
                    mVideoEditor.play();
                }
            }
        });

        ViewGroup.LayoutParams layoutParams = surfaceView.getLayoutParams();
        layoutParams.width = 1080;
        layoutParams.height = 1920;
        surfaceView.setLayoutParams(layoutParams);
        mVideoEditor.setSurfaceView(surfaceView);

        String thumbnailPath = "/sdcard/Pictures/thumbnail";
        mVideoEditor.setThumbnailDirectory(thumbnailPath);

        MediaClip clip1 = new MediaClip("/sdcard/DCIM/Camera/output.mp4");
        clip1.setStartTime(0 * 1000);
        clip1.setEndTime(120 * 1000);
        clip1.setSpeed(1.0f);
        clip1.setThumbnailInterval(500);
        clip1.setThumbnailListener(new IVideoThumbnailListener() {
            @Override
            public void onThumbnail(String videoPath, String thumbnailPath, int time, int index, int type) {
                Log.i(LogTag.TAG, "onThumbnail thumbnailPath=" + thumbnailPath+", time="+time);
                // saveThumbnail(thumbnailPath);
            }

            @Override
            public void onComplete() {
                Log.i(LogTag.TAG, "onComplete");
            }

            @Override
            public void onError(int err) {
                Log.i(LogTag.TAG, "onError err="+err);
            }
        });
        mVideoEditor.insertClip(clip1, new ClipListener() {

            @Override
            public void onSize(int width, int height) {
                super.onSize(width, height);
            }

            @Override
            public void onSuccess() {
                super.onSuccess();
                Log.i(LogTag.TAG, "insert clip1 success");
            }

            @Override
            public void onFailed(int code) {
                super.onFailed(code);
                Log.i(LogTag.TAG, "insert clip1 failed, code="+code);
            }
        });

        // if (mAudioEffectId == -1) {
        //     mAudioEffectId = mVideoEditor.addAudioEffect("{\n" +
        //             "    \"startTime\":5000,\n" +
        //             "    \"endTime\":40000,\n" +
        //             "    \"type\":2,\n" +
        //             "    \"volume\":100,\n" +
        //             "    \"startSpeed\":0.5,\n" +
        //             "    \"endSpeed\":5\n" +
        //             "}", new AudioEffectListener() {
        //
        //         @Override
        //         public void onFailed(int code) {
        //             super.onFailed(code);
        //             Log.e(LogTag.TAG, "addAudioEffect onFailed code="+code);
        //         }
        //
        //         @Override
        //         public void onSuccess(int id, int type, int clipDuration, int duration) {
        //             super.onSuccess(id, type, clipDuration, duration);
        //             Log.e(LogTag.TAG, "addAudioEffect onSuccess id="+id+", type="+type+", clipDuration="+clipDuration+", duration="+duration);
        //         }
        //     });
        // }

        final Button playPauseBtn = findViewById(R.id.btn_play_pause);
        playPauseBtn.setOnClickListener(v -> {
            if (mVideoEditor == null) {
                return;
            }
            if (mVideoEditor.isPlaying()) {
                mVideoEditor.pause();
                mVideoEditor.refreshFrame();
                playPauseBtn.setText("播放");
            } else {
                mVideoEditor.play();
                playPauseBtn.setText("暂停");
            }
        });



        // if (mMusicId == -1) {
        //     mVideoEditor.addMusic("{\n" +
        //             "    \"path\":\"/sdcard/poizon/audio/test.wav\",\n" +
        //             "    \"clipOffset\":30000,\n" +
        //             "    \"startTime\":0,\n" +
        //             "    \"endTime\":30000,\n" +
        //             "    \"loop\":false\n" +
        //             "}", new EffectListener() {
        //
        //         @Override
        //         public void onFailed(int code) {
        //             super.onFailed(code);
        //         }
        //
        //         @Override
        //         public void onEffectId(int id) {
        //             super.onEffectId(id);
        //             mMusicId = id;
        //         }
        //     });
        // }
        final Button musicBtn = findViewById(R.id.btn_music);
        musicBtn.setOnClickListener(v -> {
            if (mVideoEditor == null) {
                return;
            }
            mVideoEditor.pause();
            if (mMusicId != -1) {
                mVideoEditor.updateMusic(mMusicId, "{\n" +
                        "    \"path\":\"/sdcard/poizon/audio/test.wav\",\n" +
                        "    \"clipOffset\":5000,\n" +
                        "    \"startTime\":5000,\n" +
                        "    \"endTime\":20000,\n" +
                        "    \"loop\":false\n" +
                        "}", new EffectListener() {

                    @Override
                    public void onFailed(int code) {
                        super.onFailed(code);
                    }
                });
                musicBtn.setText("删除音乐");
                mVideoEditor.play();
            } else {
                mVideoEditor.deleteMusic(mMusicId, new EffectListener() {

                    @Override
                    public void onEffectId(int id) {
                        super.onEffectId(id);
                        mMusicId = id;
                    }

                    @Override
                    public void onFailed(int code) {
                        super.onFailed(code);
                    }
                });
                musicBtn.setText("添加音乐");
            }
        });

        findViewById(R.id.btn_export).setOnClickListener(v -> {
            /**
             * 准备导出视频
             */
            if (mVideoEditor == null) {
                return;
            }
            File draftFile = new File(mDraftPath);
            if (draftFile.exists()) {
                draftFile.delete();
            }
            String content = mVideoEditor.saveDraft();
            BufferedWriter out;
            try {
                out = new BufferedWriter(new FileWriter(mDraftPath));
                out.write(content);
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(EditorActivity.this, ExportActivity.class);
            intent.putExtra("draftPath", mDraftPath);
            startActivity(intent);

        });

        findViewById(R.id.btn_adjust_audio).setOnClickListener(v -> {
            if (mVideoEditor == null) {
                return;
            }
            MediaClip clip = mVideoEditor.getClip(0);
            clip.setVolume(30);
            mVideoEditor.updateClip(0, clip, new ClipListener() {
                @Override
                public void onFailed(int code) {
                    super.onFailed(code);
                }

                @Override
                public void onSuccess() {
                    super.onSuccess();
                }
            });
        });

        findViewById(R.id.btn_add_filter).setOnClickListener(v -> {
            if (mVideoEditor == null) {
                return;
            }
            if (mFilterId == -1) {
                mFilterId = mVideoEditor.addFilter("/sdcard/test_ve/filter/4meishi", false);
                mFilterSeekBar.setProgress(100);
            }
        });

        mFilterSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mVideoEditor == null) {
                    return;
                }
                if (mFilterId != -1) {
                    mVideoEditor.updateFilterIntensity(mFilterId, seekBar.getProgress());
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoEditor != null) {
            mVideoEditor.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoEditor != null) {
            mVideoEditor.destroy();
        }
    }

    private void saveThumbnail(String thumbnailPath) {
        File file = new File(thumbnailPath);
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            int length = is.available();
            byte[] b = new byte[4];
            is.read(b);
            int width = ByteUtils.byteArrayToInt(b);
            is.read(b);
            int height = ByteUtils.byteArrayToInt(b);
            byte[] thumb = new byte[length - 8];
            is.read(thumb);

            Bitmap thumbBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            thumbBitmap.copyPixelsFromBuffer(ByteBuffer.wrap(thumb));
            String path = file.getParentFile().getAbsolutePath() + File.separator + "pic"  + File.separator + file.getName() + ".png";
            ImageUtils.saveImage(thumbBitmap, path);
            thumbBitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {

            }
        }
    }
}
