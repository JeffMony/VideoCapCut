### 视频编辑介绍

#### 开发者API介绍
```
public interface IVideoEditor extends IEffect {

    void setVideoEditorListener(VideoEditorListener listener);

    void setVideoRenderListener(VideoRenderListener listener);

    void setThumbnailDirectory(String path);

    void setLoop(boolean loop);

    void setDraftPath(String draftPath);

    void setSurfaceView(PreviewSurfaceView view);

    long getVideoDuration();

    long getVideoTime(int index, int offsetTime);

    long getCurrentPosition();

    int getClipCount();

    int getCurrentClipIndex();

    MediaClip getClip(int index);

    void insertClip(MediaClip clip, ClipListener listener);

    void insertClip(int index, MediaClip clip, ClipListener listener);

    void updateClip(int index, MediaClip clip, ClipListener listener);

    void updateClipTime(int index, int startTime, int endTime, ClipListener listener);

    void swap(int from, int to, ClipListener listener);

    void moveTo(int from, int to, ClipListener listener);

    void removeClip(int index, ClipListener listener);

    void removeClip(int index, boolean deleteThumbnail, ClipListener listener);

    void removeAllClips(boolean deleteThumbnail, ClipListener listener);

    void replaceClip(int index, MediaClip clip, ClipListener listener);

    List<MediaClip> getClips();

    int getClipIndex(int time);

    void seek(int time);

    void seek(int index, int time);

    void seekComplete();

    void updateCurrentFrame();

    boolean isPlaying();

    void prepare();

    void prepare(int index);

    void play();

    void pause();

    void destroy();

    int capture(int width, int height, VideoCaptureListener listener);

    int refreshFrame();

    void setFrameRate(int frameRate);

    String saveDraft();

    int addAudioEffect(String config, AudioEffectListener listener);
    
    void updateAudioEffect(int id, String config, AudioEffectListener listener);

    void updateAudioEffect(int id, int startTime, int endTime, AudioEffectListener listener);

    void updateAudioEffect(int id, int volume);

    void updateAudioEffect(int id, float speed, AudioEffectListener listener);

    void updateAudioEffect(int id, float startSpeed, float endSpeed, AudioEffectListener listener);

    void deleteAudioEffect(int id, AudioEffectListener listener);

}
```

#### 缩略图体验
![缩略图应用](../files/output.gif)