### 视频特效介绍

#### 开发者API介绍
```
public interface IEffect {

    void addMusic(String config, EffectListener listener);

    void updateMusic(int id, String config, EffectListener listener);

    void deleteMusic(int id, EffectListener listener);

    int addFilter(String config);

    int addFilter(String config, boolean encrypt);

    void updateFilter(int id, String config, boolean encrypt);

    void updateFilterIntensity(int id, int intensity);

    void updateFilterTime(int id, int startTime, int endTime);

    void deleteFilter(int id);

    int addEffect(String config);

    int addEffect(String config, InputType inputType);

    void updateEffect(int id, String config, InputType type);

    void updateEffectTime(int id, int startTime, int endTime);

    void deleteEffect(int id);
}
```