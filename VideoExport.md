### 视频导出介绍

#### 开发者API介绍
```
public interface IVideoExport {

    void setVideoRenderListener(VideoExportRenderListener listener);

    int export(String config, InputType type, ExportInfo exportInfo, ExportListener listener);

    void cancel();
}
```