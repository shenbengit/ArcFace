# ArcFace
基于Android虹软人脸识别增值版SDK V3.1,封装人脸识别方法

## 引入

### Gradle:
最新版本
```gradle
//必选，默认仅支持armeabi-v7a
implementation 'com.shencoder:arc-face:1.0.0'
//可选，支持arm64-v8a
implementation 'com.shencoder:arc-face-arm64-v8a:1.0.0'
```

###### 如果Gradle出现implementation失败的情况，可以在Project的build.gradle里面添加如下：
```gradle
allprojects {
    repositories {
        //...
        maven { url 'https://dl.bintray.com/shencoder/android-lib' }
    }
}
```
