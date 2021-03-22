package com.shencoder.arcface.configuration

import android.content.Context
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import com.shencoder.arcface.callback.OnErrorCallback
import com.shencoder.arcface.callback.OnRecognizeCallback
import com.shencoder.arcface.view.MyTextureCameraPreview
import com.shencoder.arcface.view.ViewfinderView

/**
 * 人脸识别相关配置
 *
 * @author  ShenBen
 * @date    2021/02/05 15:25
 * @email   714081644@qq.com
 */
class FaceConfiguration internal constructor(builder: Builder) {
    val context: Context

    val recognizeCallback: OnRecognizeCallback?

    /**
     * 是否需要识别
     */
    val enableRecognize: Boolean

    /**
     * 人脸检测角度
     */
    val detectFaceOrient: DetectFaceOrient

    /**
     * 用于数值化表示的最小人脸尺寸，该尺寸代表人脸尺寸相对于图片长边的占比。
     * VIDEO 模式有效值范围[2,32]，推荐值为16；
     * IMAGE 模式有效值范围[2,32]，推荐值为 30；
     * 特殊情况下可根据具体场景进行设置。
     */
    val detectFaceScaleVal: Int

    /**
     * 活体检测类型，[LivenessType.IR] 目前不支持
     */
    val livenessType: LivenessType

    /**
     * 设置RGB可见光活体阈值，有效值范围(0.0f,1.0f)，推荐值为0.6f
     */
    val rgbLivenessThreshold: Float

    /**
     * 设置IR红外活体阈值，有效值范围(0.0f,1.0f)，推荐值为0.7f
     */
    val irLivenessThreshold: Float

    /**
     * 是否启用图像质量阈值
     */
    val enableImageQuality: Boolean

    /**
     * 图像质量阈值，有效值范围(0.0f,1.0f)
     */
    val imageQualityThreshold: Float

    /**
     * 最大需要检测的人脸个数，取值范围[1,50]
     */
    val detectFaceMaxNum: Int

    /**
     * 是否仅识别最大人脸
     */
    val recognizeKeepMaxFace: Boolean

    /**
     * 是否限制识别区域
     */
    val enableRecognizeAreaLimited: Boolean

    /**
     * 识别区域屏占比，默认在摄像预览画面中间，有效值范围(0.0f,1.0f)
     */
    val recognizeAreaLimitedRatio: Float

    /**
     * 相关属性检测，年龄、性别、3d角度
     */
    val detectInfo: DetectInfo

    /**
     * 彩色RGB摄像头类型
     */
    val rgbCameraFcing: CameraFacing

    /**
     * 红外IR摄像头类型
     */
    val irCameraFcing: CameraFacing

    /**
     * 摄像头预览分辨率，为null时自动计算
     * 预览分辨率是[rgbCameraFcing] [irCameraFcing] 都支持的预览分辨率
     */
    val previewSize: PreviewSize?

    /**
     * 人脸识别框绘制相关
     */
    val drawFaceRect: DrawFaceRect

    /**
     * 是否镜像预览
     */
    val isMirror: Boolean

    /**
     * 人脸特征提取出错重试次数
     */
    @IntRange(from = 1)
    val extractFeatureErrorRetryCount: Int

    /**
     * 人脸识别失败后，重试间隔，单位：毫秒
     */
    @IntRange(from = 1)
    val recognizeFailedRetryInterval: Long

    /**
     * 体检测出错重试次数
     */
    @IntRange(from = 1)
    val livenessErrorRetryCount: Int

    /**
     * 活体检测失败后，重试间隔，单位：毫秒
     */
    @IntRange(from = 1)
    val livenessFailedRetryInterval: Long

    /**
     * 是否启用人脸比对
     */
    val enableCompareFace: Boolean

    /**
     * 扫描框提示文字
     */
    val viewfinderText: String?

    /**
     * 扫描框提示文字位置
     */
    val viewfinderTextGravity: ViewfinderView.TextLocation

    /**
     * 人脸识别时异常回调
     */
    val onErrorCallback: OnErrorCallback?

    init {
        context = builder.context
        recognizeCallback = builder.recognizeCallback
        enableRecognize = builder.enableRecognize
        detectFaceOrient = builder.detectFaceOrient
        detectFaceScaleVal = builder.detectFaceScaleVal
        livenessType = builder.livenessType
        rgbLivenessThreshold = builder.rgbLivenessThreshold
        irLivenessThreshold = builder.irLivenessThreshold
        enableImageQuality = builder.enableImageQuality
        imageQualityThreshold = builder.imageQualityThreshold
        detectFaceMaxNum = builder.detectFaceMaxNum
        recognizeKeepMaxFace = builder.recognizeKeepMaxFace
        enableRecognizeAreaLimited = builder.enableRecognizeAreaLimited
        recognizeAreaLimitedRatio = builder.recognizeAreaLimitedRatio
        detectInfo = builder.detectInfo
        rgbCameraFcing = builder.rgbCameraFcing
        irCameraFcing = builder.irCameraFcing
        previewSize = builder.previewSize
        drawFaceRect = builder.drawFaceRect
        isMirror = builder.isMirror
        extractFeatureErrorRetryCount = builder.extractFeatureErrorRetryCount
        recognizeFailedRetryInterval = builder.recognizeFailedRetryInterval
        livenessErrorRetryCount = builder.livenessErrorRetryCount
        livenessFailedRetryInterval = builder.livenessFailedRetryInterval
        enableCompareFace = builder.enableCompareFace
        viewfinderText = builder.viewfinderText
        viewfinderTextGravity = builder.viewfinderTextGravity
        onErrorCallback = builder.onErrorCallback
    }

    class Builder(internal val context: Context, val recognizeCallback: OnRecognizeCallback?) {
        /**
         * 人脸检测角度
         */
        internal var detectFaceOrient: DetectFaceOrient = DetectFaceOrient.ASF_OP_0_ONLY
        fun setDetectFaceOrient(detectFaceOrient: DetectFaceOrient) =
            apply { this.detectFaceOrient = detectFaceOrient }

        /**
         * 是否需要识别
         */
        internal var enableRecognize = true
        fun enableRecognize(enableRecognize: Boolean) =
            apply { this.enableRecognize = enableRecognize }

        /**
         * 用于数值化表示的最小人脸尺寸，该尺寸代表人脸尺寸相对于图片长边的占比。
         * VIDEO 模式有效值范围[2,32]，推荐值为16；
         * IMAGE 模式有效值范围[2,32]，推荐值为 30；
         * 特殊情况下可根据具体场景进行设置。
         */
        internal var detectFaceScaleVal: Int = 16
        fun setDetectFaceScaleVal(@IntRange(from = 2, to = 32) detectFaceScaleVal: Int) =
            apply { this.detectFaceScaleVal = detectFaceScaleVal }

        /**
         * 活体检测类型，[LivenessType.IR] 目前不支持
         */
        internal var livenessType: LivenessType = LivenessType.NONE
        fun setLivenessType(livenessType: LivenessType) =
            apply { this.livenessType = livenessType }

        /**
         * 设置RGB可见光活体阈值，有效值范围(0.0f,1.0f)，推荐值为0.6f
         */
        internal var rgbLivenessThreshold: Float = 0.6f
        fun setRgbLivenessThreshold(
            @FloatRange(
                from = 0.0,
                to = 1.0,
                fromInclusive = false,
                toInclusive = false
            ) rgbLivenessThreshold: Float
        ) =
            apply { this.rgbLivenessThreshold = rgbLivenessThreshold }

        /**
         * 设置IR红外活体阈值，有效值范围(0.0f,1.0f)，推荐值为0.7f
         */
        internal var irLivenessThreshold: Float = 0.7f
        fun setIrLivenessThreshold(
            @FloatRange(
                from = 0.0,
                to = 1.0,
                fromInclusive = false,
                toInclusive = false
            ) irLivenessThreshold: Float
        ) =
            apply { this.irLivenessThreshold = irLivenessThreshold }

        /**
         * 是否启用图像质量阈值
         */
        internal var enableImageQuality = false
        fun enableImageQuality(
            enableImageQuality: Boolean
        ) =
            apply { this.enableImageQuality = enableImageQuality }

        /**
         * 图像质量阈值，有效值范围(0.0f,1.0f)
         */
        internal var imageQualityThreshold: Float = 0.35f
        fun setImageQualityThreshold(
            @FloatRange(
                from = 0.0,
                to = 1.0,
                fromInclusive = false,
                toInclusive = false
            ) imageQualityThreshold: Float
        ) =
            apply { this.imageQualityThreshold = imageQualityThreshold }

        /**
         * 最大需要检测的人脸个数，取值范围[1,50]
         */
        internal var detectFaceMaxNum = 1
        fun setDetectFaceMaxNum(@IntRange(from = 1, to = 50) detectFaceMaxNum: Int) =
            apply { this.detectFaceMaxNum = detectFaceMaxNum }

        /**
         * 是否仅识别最大人脸
         */
        internal var recognizeKeepMaxFace = true
        fun recognizeKeepMaxFace(recognizeKeepMaxFace: Boolean) =
            apply { this.recognizeKeepMaxFace = recognizeKeepMaxFace }

        /**
         * 是否限制识别区域
         */
        internal var enableRecognizeAreaLimited = false
        fun enableRecognizeAreaLimited(enableRecognizeAreaLimited: Boolean) =
            apply { this.enableRecognizeAreaLimited = enableRecognizeAreaLimited }

        /**
         * 识别区域屏占比，默认在摄像预览画面中间，有效值范围(0.0f,1.0f)
         * 限制区域为正方形
         */
        internal var recognizeAreaLimitedRatio = 0.625f
        fun setRecognizeAreaLimitedRatio(
            @FloatRange(
                from = 0.0,
                to = 1.0,
                fromInclusive = false,
                toInclusive = false
            ) recognizeAreaLimitedRatio: Float
        ) =
            apply { this.recognizeAreaLimitedRatio = recognizeAreaLimitedRatio }

        /**
         * 相关属性检测，年龄、性别、3d角度
         * 这个功能依附于 [livenessType]，需要为[LivenessType.RGB]
         */
        internal var detectInfo: DetectInfo = DetectInfo()
        fun setDetectInfo(detectInfo: DetectInfo) =
            apply {
                this.detectInfo = detectInfo
            }

        /**
         * 彩色RGB摄像头类型
         */
        internal var rgbCameraFcing = CameraFacing.BACK
        fun setRgbCameraFcing(rgbCameraFcing: CameraFacing) =
            apply { this.rgbCameraFcing = rgbCameraFcing }

        /**
         * 红外IR摄像头类型
         */
        internal var irCameraFcing = CameraFacing.FRONT
        fun setIrCameraFcing(irCameraFcing: CameraFacing) =
            apply { this.irCameraFcing = irCameraFcing }

        /**
         * 摄像头预览分辨率，为null时自动计算
         * 预览分辨率是[rgbCameraFcing] [irCameraFcing] 都支持的预览分辨率
         */
        internal var previewSize: PreviewSize? = null
        fun setPreviewSize(previewSize: PreviewSize) = apply { this.previewSize = previewSize }

        /**
         * 人脸识别框绘制相关
         */
        internal var drawFaceRect: DrawFaceRect = DrawFaceRect()
        fun setDrawFaceRect(drawFaceRect: DrawFaceRect) = apply {
            this.drawFaceRect = drawFaceRect
        }

        /**
         * 是否镜像预览
         * 仅支持[MyTextureCameraPreview]
         */
        internal var isMirror = false
        fun isMirror(isMirror: Boolean) = apply {
            this.isMirror = isMirror
        }

        /**
         * 人脸特征提取出错重试次数
         */
        @IntRange(from = 1)
        internal var extractFeatureErrorRetryCount = 3
        fun setExtractFeatureErrorRetryCount(@IntRange(from = 1) extractFeatureErrorRetryCount: Int) =
            apply {
                this.extractFeatureErrorRetryCount = extractFeatureErrorRetryCount
            }

        /**
         * 人脸识别失败后，重试间隔，单位：毫秒
         */
        @IntRange(from = 1)
        internal var recognizeFailedRetryInterval: Long = 1000
        fun setRecognizeFailedRetryInterval(@IntRange(from = 1) recognizeFailedRetryInterval: Long) =
            apply {
                this.recognizeFailedRetryInterval = recognizeFailedRetryInterval
            }

        /**
         * 体检测出错重试次数
         */
        @IntRange(from = 1)
        internal var livenessErrorRetryCount = 3
        fun setLivenessErrorRetryCount(@IntRange(from = 1) livenessErrorRetryCount: Int) = apply {
            this.livenessErrorRetryCount = livenessErrorRetryCount
        }

        /**
         * 活体检测失败后，重试间隔，单位：毫秒
         */
        @IntRange(from = 1)
        internal var livenessFailedRetryInterval: Long = 1000
        fun setLivenessFailedRetryInterval(@IntRange(from = 1) livenessFailedRetryInterval: Long) =
            apply {
                this.livenessFailedRetryInterval = livenessFailedRetryInterval
            }

        /**
         * 是否启用人脸比对
         */
        internal var enableCompareFace = true
        fun enableCompareFace(enableCompareFace: Boolean) =
            apply {
                this.enableCompareFace = enableCompareFace
            }

        /**
         * 扫描框提示文字
         */
        internal var viewfinderText: String? = null
        fun setViewfinderText(viewfinderText: String?) =
            apply {
                this.viewfinderText = viewfinderText
            }

        /**
         * 扫描框提示文字位置
         */
        internal var viewfinderTextGravity = ViewfinderView.TextLocation.BOTTOM
        fun setViewfinderGravity(viewfinderTextGravity: ViewfinderView.TextLocation) =
            apply {
                this.viewfinderTextGravity = viewfinderTextGravity
            }

        /**
         * 人脸识别时异常回调
         */
        internal var onErrorCallback: OnErrorCallback? = null
        fun setOnErrorCallback(onErrorCallback: OnErrorCallback?) =
            apply {
                this.onErrorCallback = onErrorCallback
            }

        fun build(): FaceConfiguration {
            if (recognizeCallback == null) {
                //如果识别结果回掉为空，则直接强制不启用识别操作
                enableRecognize = false
            }
            return FaceConfiguration(this)
        }
    }
}