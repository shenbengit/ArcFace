package com.shencoder.arcface.view

import android.content.Context
import android.view.ViewGroup
import com.otaliastudios.cameraview.preview.TextureCameraPreview
import com.otaliastudios.cameraview.size.AspectRatio
import kotlin.math.abs

/**
 *
 * @author  ShenBen
 * @date    2021/02/26 11:39
 * @email   714081644@qq.com
 */
class MyTextureCameraPreview(context: Context, group: ViewGroup) :
    TextureCameraPreview(context, group) {

    private var isMirror = false

    internal fun setMirror(isMirror: Boolean) {
        if (this.isMirror == isMirror) {
            return
        }
        this.isMirror = isMirror
    }

    override fun crop(callback: CropCallback?) {
        view.post(Runnable {
            if (mInputStreamHeight == 0 || mInputStreamWidth == 0 || mOutputSurfaceHeight == 0 || mOutputSurfaceWidth == 0) {
                callback?.onCrop()
                return@Runnable
            }
            var scaleX = 1f
            var scaleY = 1f
            val current = AspectRatio.of(mOutputSurfaceWidth, mOutputSurfaceHeight)
            val target = AspectRatio.of(mInputStreamWidth, mInputStreamHeight)
            if (current.toFloat() >= target.toFloat()) {
                // We are too short. Must increase height.
                scaleY = current.toFloat() / target.toFloat()
            } else {
                //
                scaleX = target.toFloat() / current.toFloat()
            }
            scaleX = if (isMirror) -scaleX else scaleX
            view.scaleX = scaleX
            view.scaleY = scaleY
            mCropping = abs(scaleX) > 1.02f || scaleY > 1.02f
            LOG.i("crop:", "applied scaleX=", scaleX)
            LOG.i("crop:", "applied scaleY=", scaleY)
            callback?.onCrop()
        })

    }
}