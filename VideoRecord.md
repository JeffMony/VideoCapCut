### 相机介绍

#### 开发者API介绍
```
public interface IVideoRecord extends IEffect {

    void setVideoRenderListener(VideoRenderListener listener);

    void setRecordListener(RecordListener listener);

    void setOnCameraStateCallback(OnCameraStateCallback callback);

    void setSurfaceView(PreviewSurfaceView surfaceView);

    void setPreviewResolution(Resolution resolution);

    void setPictureResolution(Resolution resolution);

    void setEncodeFrameType(EncodeFrameType type);

    void startPreview();

    void stopPreview();

    void setSpeed(float speed);

    /**
     * VideoRecord 支持录制多段视频, 然后自动合并
     * 删除上一段
     */
    void deletePrevious();

    /**
     * 合并录制的多个视频
     * @param outputPath
     * @param listener
     * @return
     */
    int composite(String outputPath, VideoCompositeListener listener);

    List<MediaClip> getClips();

    void musicSeekTo(int musicId, int time);

    void switchCamera();

    void setCameraFacing(Facing facing);

    Facing getCameraFacing();

    /**
     * 设置闪光灯
     * @param flash
     */
    void setCameraFlash(Flash flash);

    /**
     * 手动聚焦区域
     * @param pointF
     */
    void focus(PointF pointF, OnFocusCallback callback);

    int startRecord(ExportInfo exportInfo);

    void stopRecord();

    boolean isRecording();

    int takePicture(TakePictureListener listener);

    void queueEvent(Runnable runnable);

    void destroy();
}
```