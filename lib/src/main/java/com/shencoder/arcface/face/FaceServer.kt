package com.shencoder.arcface.face

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.IntRange
import com.arcsoft.face.*
import com.arcsoft.face.enums.DetectFaceOrientPriority
import com.arcsoft.face.enums.DetectMode
import com.arcsoft.imageutil.ArcSoftImageFormat
import com.arcsoft.imageutil.ArcSoftImageUtil
import com.arcsoft.imageutil.ArcSoftImageUtilError
import com.shencoder.arcface.configuration.DetectFaceOrient
import com.shencoder.arcface.configuration.FaceFeatureDataBean
import com.shencoder.arcface.face.model.CompareResult
import com.shencoder.arcface.util.LogUtil
import java.util.*

/**
 * 用于人脸搜索、生成特征码
 * 可以自己封装成单例模式
 *
 * @author  ShenBen
 * @date    2021/02/24 14:08
 * @email   714081644@qq.com
 */
class FaceServer {
    companion object {
        private const val TAG = "FaceServer->"
    }

    private val faceEngine = FaceEngine()

    private val lock = Object()

    /**
     * 初始化人脸引擎
     * @param context 上下文
     * @param faceOrient 人脸检测角度，单一角度检测，不支持[DetectFaceOrient.ASF_OP_ALL_OUT]
     * [DetectFaceOrient.ASF_OP_0_ONLY]
     * [DetectFaceOrient.ASF_OP_90_ONLY]
     * [DetectFaceOrient.ASF_OP_180_ONLY]
     * [DetectFaceOrient.ASF_OP_270_ONLY]
     * @param detectFaceScaleVal 识别的最小人脸比例，取值范围[2,32]
     */
    fun init(
        context: Context,
        faceOrient: DetectFaceOrient = DetectFaceOrient.ASF_OP_0_ONLY,
        @IntRange(from = 2, to = 32)
        detectFaceScaleVal: Int = 16
    ) {
        val orientPriority =
            if (faceOrient == DetectFaceOrient.ASF_OP_ALL_OUT) {
                DetectFaceOrientPriority.ASF_OP_0_ONLY
            } else {
                DetectFaceOrientPriority.valueOf(faceOrient.name)
            }
        val result = faceEngine.init(
            context,
            DetectMode.ASF_DETECT_MODE_IMAGE,
            orientPriority,
            detectFaceScaleVal,
            1,
            FaceEngine.ASF_FACE_DETECT or FaceEngine.ASF_FACE_RECOGNITION
        )
        LogUtil.i("${TAG}人脸比对引擎初始化:$result")
    }

    /**
     * 比对人脸，会返回相似度最大的[features]中的数据
     * @param faceFeature 要比对的人脸特征码
     * @param features 待比对的人脸列表
     *
     * @return null 说明比对未通过
     */
    fun compareFaceFeature(
        faceFeature: FaceFeature,
        features: List<FaceFeatureDataBean>
    ): CompareResult? {
        if (features.isEmpty()) {
            return null
        }
        val tempFaceFeature = FaceFeature()
        val faceSimilar = FaceSimilar()
        var maxSimilar = 0f
        var maxSimilarIndex = -1

        synchronized(lock) {
            features.forEachIndexed { index, bean ->
                tempFaceFeature.featureData = bean.feature
                val result =
                    faceEngine.compareFaceFeature(faceFeature, tempFaceFeature, faceSimilar)
                if (result == ErrorInfo.MOK) {
                    if (faceSimilar.score > maxSimilar) {
                        maxSimilar = faceSimilar.score
                        maxSimilarIndex = index
                    }
                } else {
                    LogUtil.e("${TAG}compareFaceFeature-errorCode: $result")
                }
            }
        }
        if (maxSimilarIndex != -1) {
            return CompareResult(features[maxSimilarIndex], maxSimilar)
        }
        return null
    }

    /**
     * 比对两组特征码
     * @return 返回相似度
     */
    fun compareFaceFeature(feature1: ByteArray, feature2: ByteArray): Float {
        val faceFeature1 = FaceFeature(feature1)
        val faceFeature2 = FaceFeature(feature2)
        val similar = FaceSimilar()
        synchronized(faceEngine) {
            val result = faceEngine.compareFaceFeature(faceFeature1, faceFeature2, similar)
            if (result != ErrorInfo.MOK) {
                LogUtil.e("${TAG}compareFaceFeature-errorCode: $result")
            }
        }
        return similar.score
    }

    /**
     * 通过Bitmap提取特征码
     * 需要在子线程运行，避免主线程卡死
     */
    fun extractFaceFeature(bitmap: Bitmap?): ByteArray? {
        if (bitmap == null) {
            return null
        }
        var feature: ByteArray? = null
        val alignedBitmap = ArcSoftImageUtil.getAlignedBitmap(bitmap, true)
        val imageData = ArcSoftImageUtil.createImageData(
            alignedBitmap.width,
            alignedBitmap.height,
            ArcSoftImageFormat.BGR24
        )
        val code = ArcSoftImageUtil.bitmapToImageData(
            alignedBitmap,
            imageData,
            ArcSoftImageFormat.BGR24
        )
        if (code == ArcSoftImageUtilError.CODE_SUCCESS) {
            //人脸检测
            val faceInfoList: List<FaceInfo> = ArrayList()
            synchronized(faceEngine) {
                val detectFaceResult = faceEngine.detectFaces(
                    imageData,
                    alignedBitmap.width,
                    alignedBitmap.height,
                    FaceEngine.CP_PAF_BGR24,
                    faceInfoList
                )
                if (detectFaceResult == ErrorInfo.MOK && faceInfoList.isNotEmpty()) {
                    val faceFeature = FaceFeature()
                    val extractResult = faceEngine.extractFaceFeature(
                        imageData,
                        alignedBitmap.width,
                        alignedBitmap.height,
                        FaceEngine.CP_PAF_BGR24,
                        faceInfoList[0],
                        faceFeature
                    )
                    if (extractResult == ErrorInfo.MOK) {
                        feature = faceFeature.featureData
                    } else {
                        LogUtil.e("${TAG}extractFaceFeature-extractFaceFeature: $extractResult")
                    }
                } else {
                    LogUtil.e("${TAG}extractFaceFeature-detectFaces: $detectFaceResult")
                }
            }
        } else {
            LogUtil.e("${TAG}extractFaceFeature-bitmapToImageData: $code")
        }
        return feature
    }

    fun destroy() {
        synchronized(faceEngine) {
            val result = faceEngine.unInit()
            LogUtil.w("${TAG}destroy-faceEngine.unInit:$result")
        }
    }
}