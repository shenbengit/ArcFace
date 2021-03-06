package com.shencoder.arcface.callback

import android.hardware.Camera

/**
 * 摄像头相关操作
 *
 * @author  ShenBen
 * @date    2021/03/05 10:57
 * @email   714081644@qq.com
 */
interface OnCameraListener {
    /**
     * 摄像头开启
     */
    fun onCameraOpened() {

    }

    /**
     * 摄像头关闭
     */
    fun onCameraClosed() {

    }

    /**
     * 方向变化
     * @param orientation 值为 0, 90, 180 或 270
     */
    fun onOrientationChanged(orientation: Int) {

    }

    /**
     * 摄像头预览数据回调
     * 仅支持[Camera] 预览数据回调
     *
     * 运行在子线程
     *
     * @param nv21 摄像头预览数据
     */
    fun onFrameProcessor(nv21: ByteArray) {

    }

    /**
     * 摄像头开启异常
     */
    fun onCameraError(exception: Exception) {

    }
}