package com.shencoder.arcface.callback

import com.shencoder.arcface.configuration.FaceFeatureDataBean
import com.shencoder.arcface.configuration.FaceFeatureDataSource
import com.shencoder.arcface.face.model.RecognizeInfo

/**
 * 人脸识别结果回调
 *
 * @author ShenBen
 * @date 2020/12/17 9:17
 * @email 714081644@qq.com
 */
interface OnRecognizeCallback : FaceFeatureDataSource {
    /**
     * 检测到的人脸数量
     */
    fun detectFaceNum(num: Int, faceIds: List<Int>) {

    }

    /**
     * 有人
     * <p>运行在子线程</p>
     */
    fun someone() {

    }

    /**
     * 无人
     * <p>运行在子线程</p>
     */
    fun nobody() {

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
    fun onRecognized(
        bean: FaceFeatureDataBean,
        similar: Float,
        recognizeInfo: RecognizeInfo
    ): String?
}