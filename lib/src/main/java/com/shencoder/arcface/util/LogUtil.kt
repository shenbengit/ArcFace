package com.shencoder.arcface.util

import android.util.Log

/**
 *
 * @author  ShenBen
 * @date    2021/02/24 17:25
 * @email   714081644@qq.com
 */
object LogUtil {
    private const val TAG = "ArcFace"
    var DEBUG = true
    fun v(msg: String) {
        if (DEBUG) {
            Log.v(TAG, msg)
        }
    }

    fun d(msg: String) {
        if (DEBUG) {
            Log.d(TAG, msg)
        }
    }

    fun i(msg: String) {
        if (DEBUG) {
            Log.i(TAG, msg)
        }
    }

    fun w(msg: String) {
        if (DEBUG) {
            Log.w(TAG, msg)
        }
    }

    fun e(msg: String) {
        if (DEBUG) {
            Log.e(TAG, msg)
        }
    }
}