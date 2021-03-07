# ArcFace
基于虹软人脸识别增值版Android SDK V3.1,封装人脸识别方法

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

## 使用事例

布局示例
```Xml
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.shencoder.arcface.view.FaceCameraView
        android:id="@+id/faceCameraView"
        android:layout_width="match_parent"
        android:layout_height="450dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

代码示例
```kotlin
    val faceCameraView: FaceCameraView = findViewById(R.id.faceCameraView)
    val configuration = FaceConfiguration.Builder(this, object : OnRecognizeCallback {
        /**
         * 检测到的人脸数量
         * <p>运行在子线程</p>
         *
         * @param num 人脸数量
         * @param faceIds faceId
         */
        override fun detectFaceNum(num: Int, faceIds: List<Int>) {

        }

        /**
         * 有人，仅在有变化时调用一次
         * <p>运行在子线程</p>
         */
        override fun someone() {

        }

        /**
         * 无人，仅在有变化时调用一次
         * <p>运行在子线程</p>
         */
        override fun nobody() {

        }

        /**
         * 识别成功后结果回调，仅回调一次，直到人脸离开画面
         * <p>运行在子线程</p>
         *
         * @param bean 识别的数据 [faceFeatureList] 的子项
         * @param similar 识别通过的相似度
         * @param recognizeInfo 识别到的其他信息，包含活体值、年龄、性别、人脸角度等信息
         * @return 人脸绘制框上成功时绘制的文字
         */
        override fun onRecognized(
            bean: FaceFeatureDataBean,
            similar: Float,
            recognizeInfo: RecognizeInfo
        ): String? {
            println("人脸比对成功-相似度:" + similar + ",recognizeInfo:" + recognizeInfo.toString())
            return "识别成功"
        }

        /**
         * 识别相似度阈值，有效值范围(0.0f,1.0f)
         */
        override fun similarThreshold(): Float {
            return 0.8f
        }

        /**
         * 待比较人脸数据集合，需要自己封装传入
         */
        override fun faceFeatureList(): List<FaceFeatureDataBean> {
            return listOf()
        }

    })
        .setDetectFaceOrient(DetectFaceOrient.ASF_OP_0_ONLY)//人脸检测角度
        .enableRecognize(true)//是否需要识别
        .setDetectFaceScaleVal(16)//用于数值化表示的最小人脸尺寸，该尺寸代表人脸尺寸相对于图片长边的占比。
        .setLivenessType(LivenessType.RGB)//活体检测类型，[LivenessType.IR] 目前不支持
        .setRgbLivenessThreshold(0.6f)//设置RGB可见光活体阈值
        .setIrLivenessThreshold(0.7f)//设置IR红外活体阈值
        .enableImageQuality(true)//是否启用图像质量阈值
        .setImageQualityThreshold(0.35f)//图像质量阈值
        .setDetectFaceMaxNum(1)//最大需要检测的人脸个数
        .recognizeKeepMaxFace(true)//是否仅识别最大人脸
        .enableRecognizeAreaLimited(false)//是否限制识别区域
        .setRecognizeAreaLimitedRatio(0.7f)//识别区域屏占比，正方形，位置在预览画面正中间
        .setDetectInfo(
            DetectInfo(
                age = false,
                gender = false,
                angle = false
            )
        )//相关属性检测，年龄、性别、3d角度这个功能依附于 [livenessType]，需要为[LivenessType.RGB]
        .setRgbCameraFcing(CameraFacing.BACK)//彩色RGB摄像头类型
        .setIrCameraFcing(CameraFacing.FRONT)//红外IR摄像头类型，目前不支持
        .setPreviewSize(PreviewSize(1280, 720))//摄像头预览分辨率，彩色摄像头和红外都支持的预览分辨率
        .setDrawFaceRect(
            DrawFaceRect(
                isDraw = true,
                unknownColor = Color.YELLOW,
                failedColor = Color.RED,
                successColor = Color.GREEN
            )
        )//人脸识别框绘制相关
        .isMirror(true)//预览画面是否镜像
        .setExtractFeatureErrorRetryCount(3)//人脸特征提取出错重试次数，超过置为失败状态
        .setRecognizeFailedRetryInterval(1000)//人脸识别失败后，重试间隔，单位：毫秒
        .setLivenessErrorRetryCount(3)//体检测出错重试次数
        .setLivenessFailedRetryInterval(1000)//活体检测失败后，重试间隔，单位：毫秒
        .setOnErrorCallback(object : OnErrorCallback {
            override fun onError(type: FaceErrorType, errorCode: Int, errorMessage: String) {
                Log.e(
                    "MainActivity",
                    "onError: FaceErrorType:${type},errorCode:${errorCode},errorMessage:${errorMessage}"
                )
            }
        })//识别中错误回调
        .build()
    //设置人脸相关参数，如果确认人脸已经激活且直接进行人脸识别则设备true
    faceCameraView.setConfiguration(configuration, false)
    faceCameraView.setLifecycleOwner(this)
    //在合适的地方调用此方法，设置为true且人脸已激活才会提交预览数据
    faceCameraView.enableFace(true)
```
