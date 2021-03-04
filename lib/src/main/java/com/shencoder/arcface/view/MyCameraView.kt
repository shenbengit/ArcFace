package com.shencoder.arcface.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.controls.Engine
import com.otaliastudios.cameraview.controls.Preview
import com.otaliastudios.cameraview.engine.CameraEngine
import com.otaliastudios.cameraview.preview.CameraPreview
import com.otaliastudios.cameraview.preview.GlCameraPreview
import com.otaliastudios.cameraview.preview.SurfaceCameraPreview

/**
 *
 * @author  ShenBen
 * @date    2021/02/23 17:24
 * @email   714081644@qq.com
 */
internal class MyCameraView(context: Context, attrs: AttributeSet? = null) :
    CameraView(context, attrs) {

    private lateinit var mCameraEngine: CameraEngine
    private lateinit var mCameraPreview: CameraPreview<*, *>

    /**
     * 仅支持初始设置
     * 是否镜像
     * 仅支持[MyTextureCameraPreview]
     */
    private var isMirror = false

    internal fun setMirror(isMirror: Boolean) {
        if (this.isMirror == isMirror) {
            return
        }
        this.isMirror = isMirror
    }

    override fun instantiateCameraEngine(
        engine: Engine,
        callback: CameraEngine.Callback
    ): CameraEngine {
        mCameraEngine = super.instantiateCameraEngine(engine, callback)
        return mCameraEngine
    }

    internal fun getCameraEngine() = mCameraEngine

    override fun instantiatePreview(
        preview: Preview,
        context: Context,
        container: ViewGroup
    ): CameraPreview<*, *> {
        mCameraPreview = when (preview) {
            Preview.SURFACE -> {
                SurfaceCameraPreview(context, container)
            }
            Preview.TEXTURE -> {
                if (isHardwareAccelerated) {
                    MyTextureCameraPreview(context, container).apply { setMirror(isMirror) }
                } else {
                    GlCameraPreview(context, container)
                }
            }
            Preview.GL_SURFACE -> {
                GlCameraPreview(context, container)
            }
        }
        return mCameraPreview
    }

    internal fun getCameraPreview() = mCameraPreview

}