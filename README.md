### 音视频编辑软件
#### 架构介绍
![VE软件架构](./files/VE软件架构.png)

- 特效模块是公用的，视频编辑可以用，相机也可以用
- 支持软硬编解码
- 支持H264和HEVC编码
- 支持并行导出

#### SDK包
具体见：<br>
https://github.com/JeffMony/VideoCapCut/blob/main/app/libs/test_ve_2.7.0.aar <br>

具体可以咨询: jeffmony@163.com 

#### 特效
[视频特效介绍](./VideoEffect.md)

- 贴纸
- 色彩调节
- 滤镜
- 蒙版
- 过渡虚化
- 转场
- 画中画
- 花字
- 分镜/四分镜

#### 相机预览/拍照/拍视频
[相机介绍](./VideoRecord.md)

- 相机预览
- 比例适配
- 拍照
- 拍视频
- 各种特效

#### 视频预览/编辑
[视频编辑介绍](./VideoEditor.md)

- 视频和图片的预览
- 比例适配
- 添加音乐
- 各种特效
- 抽取视频缩略图

#### 视频编码导出
[视频导出介绍](./VideoExport.md)

- 并行导出
- 可以设置的参数：
  - GOP
  - 码率
  - 帧率
  - 宽高
  - 软硬编解码
- x264和x265编码

#### 音频播放器
- 播放音乐
- 循环播放
- 设置音量
- 倍速

### 音视频开发必备技能
- MediaCodec
- FFmpeg
- Camera
- OpenGL
- OpenCV
- RTMP/HLS/RTP
- TCP/UDP
- WebRTC
- ijkplayer
- gpuimage
- filament

### 视频补帧
- 光流法
- 帧融合

### 视频超分
- 先学习一下tensorflow
  - https://github.com/opencv/opencv
  - https://github.com/opencv/opencv_contrib
  

### 人脸检测
- Android系统原生的FaceDetector
  - https://developer.android.com/reference/android/media/FaceDetector
  - https://blog.csdn.net/yubo_725/article/details/46051573
  


