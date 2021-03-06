package com.shencoder.arcfacedemo

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import com.shencoder.arcface.callback.OnActiveCallback
import com.shencoder.arcface.callback.OnErrorCallback
import com.shencoder.arcface.callback.OnRecognizeCallback
import com.shencoder.arcface.configuration.CameraFacing
import com.shencoder.arcface.configuration.FaceConfiguration
import com.shencoder.arcface.configuration.FaceFeatureDataBean
import com.shencoder.arcface.configuration.LivenessType
import com.shencoder.arcface.constant.FaceErrorType
import com.shencoder.arcface.face.FaceActive
import com.shencoder.arcface.face.FaceServer
import com.shencoder.arcface.face.model.RecognizeInfo
import com.shencoder.arcface.util.FeatureCovertUtil
import com.shencoder.arcface.view.FaceCameraView
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var faceCameraView: FaceCameraView
    private val faceServer: FaceServer by lazy { FaceServer().apply { init(this@MainActivity) } }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        faceCameraView = findViewById(R.id.faceCameraView)
        findViewById<Button>(R.id.btn).setOnClickListener {
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_person)
            val feature1 = faceServer.extractFaceFeature(bitmap)
            if (feature1 != null) {
                val byteArrayToHexString = FeatureCovertUtil.byteArrayToHexString(feature1)
                println("提取的人脸特征码:$byteArrayToHexString")
            }

            val bitmapToBase64 = BitmapUtil.bitmapToBase64(bitmap)
            val base64ToBitmap = BitmapUtil.base64ToBitmap(bitmapToBase64!!)
            val feature2 = faceServer.extractFaceFeature(base64ToBitmap)
            if (feature2 != null) {
                val byteArrayToHexString = FeatureCovertUtil.byteArrayToHexString(feature2)
                println("提取的人脸特征码:$byteArrayToHexString")
            }
            val compareFaceFeature = faceServer.compareFaceFeature(feature1!!, feature2!!)
            println("相似度:" + compareFaceFeature)
        }
        val str =
            "0080FA440000A0412B0B1D3D032EC63C69980FBEF7EE8EBDAAB0E63CE32EF8BC9293243D13020BBDFC12BABD171DF53C4103173DE225653D6D0AD03DFF3518BB5BDBE0BD1447013CBCFA563D0D9B0ABD47BA6E3DE5BB463DC1198FBDA38800BE4AFF1D3DA402A83DEF2F76BD709CF93C9507073C4C4191BD59E5CF3CBCB42C3D7D899A3C8212C53D4BB5053E364C123EC11F933DBA9F823C9C10E2BC121AD53D3A6EC7BCB59E4EBD5726F13CF547A93D25CB733DF05A683DE7A2C0BD668B33BDD987073DC406A03CBC1C08BD7B2F46BD59C3483D3115D1BDB18BBFBA1D82273DBBED0CBCD94A203D80710FBCA99A983DA0083C3DDE1E6A3DFDC062BC62FD943D62C92A3D76A53ABD0A4D4B3D11CB5ABB910E333D80E5C63C747487BD579E90BB39650DBD00EA06BE0CF1D1BB41FF2EBD52874A3D11B537BDEE4436BE21F4513CAF88D93C9F2C7CBD322F063E16A1A33CF22DCEBCB100243D2C65753DD9FB86BD9C9C163D7E6C3B3DF91405BDD6B0AFBD62A817BE1ADD47BDCEFAA7BD8148073D679A4ABD0FA2A13DCB2C50BDA9AE873D66916A3DF4B4063DD4526D3C6D2129BC53CDD53D3693763DA50DFB3D92A9C03D3079FFBD512A153DDEB6E83D8C71BE3D3DD535BD0769ADBC194F093EFA8A8CBBDB3DCDBA30B6DD3DB540D0BCA0B44EBD0E4106BDE491883DDD0DF5BC205BAC3CD58EF6BB266DDFBB7DE955BDE7FD2B3D8CA614BC1A693C3DF451923C44482CBD715D8EBDA61E03BD94941C3C6687523DF06BABBD7E2235BBAAC71E3CAE0A5F3B3EC3443D308D643CED7A02BBCD5E703DA3A0B23DFF5E01BDB5AE553D012B4E3C4DD1C03D666AE43C03F3543D4DCA0B3BE9719EBDEC89ABBD892DAA3B0CA70ABD3E70F5BCB437693DE9BAF6BC6D4AB4BCC1DB00BE3FDA94BB2D4A2B3DCA1615BE9352C03C452722BD1C74C6BDFB2CD1BD3A6EBCBD9C63C6BC8E4B9DBD29BC16BD71736FBD8BD2EFBBCDF63D3D41A6063D9C01693D8683B6BC95C945BD204AA2BD82C272BC34E0B53C57BC04BEE3282EBDB9E7DDBCFE17013EEEA0C33D148EAABD776BCF3C9CC510BD1DCC1BBC727C183DE7B3C93D7F54EABCC6DAC4BDC3FF90BCEDD1A73DAC2E0F3D7F3BC43D8A30CCBDC4CE9CBC819FA2BC7D73643D531849BD5C45EF3DF6DE0A3EBC4971BCB99EB4BDD92B01BDF3B517BDE4F6023CC3F7283D8EB955BD90424E3DDB45273BE7EFB23B2DFBAF3CFBBD6ABDEB0158BC8345C4BCE5F0D63D4BE1E8BBC6CC08BDA414CEBC139CC93C3C5626BD859250BC9351CCBD21BB45BD8CD0B53D94B5B13DB03D60BD1DB5233D36AF80BB5BEAA1BDAB27B6BCE525BB3C6E52FA3D03A32CBD214496BD2B7764BD4F0D8DBBE19CC63CEA4634BDDEA5D3BD86FFBDBD7E68773DF488FA3DF7FB2B3DFFD8193D6D8AB53C05FC973D9BF98E3BF1EE423D6EC7ECBC611FB73C24BFDC3C895DB93D"
        val build = FaceConfiguration.Builder(this, object : OnRecognizeCallback {
            override fun detectFaceNum(num: Int, faceIds: List<Int>) {
            }

            override fun someone() {
            }

            override fun nobody() {
            }

            override fun onRecognized(
                bean: FaceFeatureDataBean,
                similar: Float,
                recognizeInfo: RecognizeInfo
            ): String? {
                println("人脸比对成功-相似度:" + similar + ",recognizeInfo:" + recognizeInfo.toString())
                return "识别成功"
            }

            override fun similarThreshold(): Float {
                return 0.8f
            }

            override fun faceFeatureList(): List<FaceFeatureDataBean> {
                return listOf(
                    FaceFeatureDataBean(
                        Any(),
                        FeatureCovertUtil.hexStringToByteArray(str)
                    )
                )
            }

        })
            .setRgbCameraFcing(CameraFacing.BACK)
            .setLivenessType(LivenessType.RGB)
            .enableImageQuality(true)
            .setRecognizeAreaLimitedRatio(0.7f)
            .setDetectFaceMaxNum(1)
            .recognizeKeepMaxFace(true)
            .isMirror(true)
            .enableRecognizeAreaLimited(false)
            .setOnErrorCallback(object : OnErrorCallback {
                override fun onError(type: FaceErrorType, errorCode: Int, errorMessage: String) {
                    Log.e(
                        "MainActivity",
                        "onError: FaceErrorType:${type},errorCode:${errorCode},errorMessage:${errorMessage}"
                    )
                }
            })
            .build()
        faceCameraView.setConfiguration(build)
        faceCameraView.setLifecycleOwner(this)

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            val activated = FaceActive.isActivated(this)
            if (activated.not()) {
                FaceActive.activeOffline(
                    this,
                    Environment.getExternalStorageDirectory().absolutePath + File.separator + "active_result.dat",
                    object : OnActiveCallback {
                        override fun activeCallback(isSuccess: Boolean, code: Int) {
                            if (isSuccess) {
                                faceCameraView.enableFace(true)
                            }
                            println("人脸激活是否成功:" + isSuccess + ",code:" + code + ",threadName:" + Thread.currentThread().name)
                        }
                    })
            } else {
                faceCameraView.enableFace(true)
            }
        }, 3000)
    }

    override fun onDestroy() {
        super.onDestroy()
        faceServer.destroy()
    }
}
