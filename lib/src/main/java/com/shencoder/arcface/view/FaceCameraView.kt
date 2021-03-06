package com.shencoder.arcface.view

import android.content.Context
import android.graphics.Rect
import android.media.Image
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.arcsoft.face.LivenessInfo
import com.otaliastudios.cameraview.CameraException
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraOptions
import com.otaliastudios.cameraview.controls.Facing
import com.otaliastudios.cameraview.size.SizeSelectors
import com.shencoder.arcface.R
import com.shencoder.arcface.callback.OnCameraListener
import com.shencoder.arcface.callback.OnPreviewCallback
import com.shencoder.arcface.configuration.CameraFacing
import com.shencoder.arcface.configuration.FaceConfiguration
import com.shencoder.arcface.face.FaceHelper
import com.shencoder.arcface.face.model.FacePreviewInfo
import com.shencoder.arcface.constant.RecognizeStatus
import com.shencoder.arcface.face.FaceActive
import com.shencoder.arcface.util.LogUtil

/**
 * 人脸识别CameraView
 *
 * @author  ShenBen
 * @date    2021/02/05 15:16
 * @email   714081644@qq.com
 */
class FaceCameraView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), LifecycleObserver, OnPreviewCallback {
    companion object {
        private const val TAG = "FaceCameraView->"
    }

    private val cameraView: MyCameraView
    private val faceRectView: FaceRectView
    private val viewfinderView: ViewfinderView
    private lateinit var mFaceConfiguration: FaceConfiguration
    private lateinit var faceHelper: FaceHelper

    private var mLifecycle: Lifecycle? = null

    /**
     * 是否启用人脸
     */
    @Volatile
    private var enableFace = false
    private var cameraListener: OnCameraListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.camera_preview, this)
        cameraView = findViewById(R.id.previewView)
        faceRectView = findViewById(R.id.faceRectView)
        viewfinderView = findViewById(R.id.viewfinderView)
        cameraView.addCameraListener(object : CameraListener() {

            override fun onCameraError(exception: CameraException) {
                cameraListener?.onCameraError(exception)
                LogUtil.e("${TAG}onCameraError:${exception.message},reason:${exception.reason}")
            }

            override fun onCameraOpened(options: CameraOptions) {
                cameraListener?.onCameraOpened()
            }

            override fun onCameraClosed() {
                cameraListener?.onCameraClosed()
            }


            override fun onOrientationChanged(orientation: Int) {
                cameraListener?.onOrientationChanged(orientation)
            }
        })

        cameraView.addFrameProcessor {
            if (it.dataClass == ByteArray::class.java) {
                cameraListener?.onFrameProcessor(it.getData())
                if (enableFace.not()) {
                    return@addFrameProcessor
                }
                //Camera Api 预览数据
                faceHelper.onPreviewFrame(
                    it.getData(),
                    it.size.width,
                    it.size.height,
                    width,
                    height
                )
            } else if (it.dataClass == Image::class.java) {
                //Camera2 Api 预览数据

            }
        }
    }

    fun setOnCameraListener(listener: OnCameraListener) {
        cameraListener = listener
    }

    fun enableFace(enableFace: Boolean) {
        if (enableFace) {
            if (this::faceHelper.isInitialized.not()) {
                val result = initFaceHelper()
                if (result.not()) {
                    return
                }
            }
        }
        this.enableFace = enableFace
    }

    /**
     * 必须要设置配置信息参数
     * 如果sdk未激活则不初始化[FaceHelper]
     */
    fun setConfiguration(configuration: FaceConfiguration) {
        cameraView.facing = getCameraFacing(configuration.rgbCameraFcing)
        configuration.previewSize?.let {
            cameraView.setPreviewStreamSize(SizeSelectors.withFilter { size -> it.width == size.width && it.height == size.height })
        }
        cameraView.setMirror(configuration.isMirror)

        faceRectView.visibility = if (configuration.drawFaceRect.isDraw) VISIBLE else INVISIBLE
        viewfinderView.visibility =
            if (configuration.enableRecognizeAreaLimited) VISIBLE else INVISIBLE
        viewfinderView.setFrameRatio(configuration.recognizeAreaLimitedRatio)

        mFaceConfiguration = configuration

        initFaceHelper()
    }

    fun setLifecycleOwner(owner: LifecycleOwner?) {
        clearLifecycleObserver()
        owner?.let {
            mLifecycle = owner.lifecycle.apply { addObserver(this@FaceCameraView) }
        }
    }

    private fun initFaceHelper(): Boolean {
        return if (FaceActive.isActivated(context)) {
            destroyFace()
            faceHelper = FaceHelper(mFaceConfiguration, this)
            true
        } else {
            LogUtil.w("${TAG}initFaceHelper-人脸识别未激活")
            false
        }
    }

    private fun clearLifecycleObserver() {
        mLifecycle?.removeObserver(this)
        mLifecycle = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun open() {
        cameraView.open()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun close() {
        cameraView.close()
        faceRectView.clearFaceInfo()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() {
        cameraView.destroy()
        cameraListener = null
        destroyFace()
    }

    private fun destroyFace() {
        if (this::faceHelper.isInitialized) {
            faceHelper.destroy()
        }
    }

    private fun getCameraFacing(facing: CameraFacing): Facing {
        return when (facing) {
            CameraFacing.BACK -> {
                Facing.BACK
            }
            CameraFacing.FRONT -> {
                Facing.FRONT
            }
        }
    }

    /**
     * 获取识别限制区域
     */
    override fun getRecognizeAreaRect(): Rect {
        return if (mFaceConfiguration.enableRecognizeAreaLimited) {
            viewfinderView.getFrameRect()
        } else {
            Rect(
                0,
                0,
                if (width == 0) Int.MAX_VALUE else width,
                if (height == 0) Int.MAX_VALUE else height
            )
        }
    }

    override fun onPreviewFaceInfo(previewInfoList: List<FacePreviewInfo>) {
        if (mFaceConfiguration.drawFaceRect.isDraw) {
            val newList = mutableListOf<FaceRectView.DrawInfo>()
            for (previewInfo in previewInfoList) {
                val recognizeInfo = faceHelper.getRecognizeInfo(previewInfo.faceId)
                var color: Int = mFaceConfiguration.drawFaceRect.colorUnknown
                when {
                    recognizeInfo.recognizeStatus == RecognizeStatus.SUCCEED -> {
                        color = mFaceConfiguration.drawFaceRect.successColor
                    }
                    recognizeInfo.recognizeStatus == RecognizeStatus.FAILED
                            || recognizeInfo.liveness == LivenessInfo.NOT_ALIVE -> {
                        color = mFaceConfiguration.drawFaceRect.failedColor
                    }

                }
                val msg = recognizeInfo.msg ?: previewInfo.faceId.toString()
                newList.add(
                    FaceRectView.DrawInfo(
                        previewInfo.rgbTransformedRect,
                        recognizeInfo.gender,
                        recognizeInfo.age,
                        recognizeInfo.liveness,
                        msg,
                        color
                    )
                )
            }
            faceRectView.drawRealtimeFaceInfo(newList)
        }
    }

}