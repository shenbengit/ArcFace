package com.shencoder.arcface.configuration

/**
 *
 * @author  ShenBen
 * @date    2021/03/02 9:35
 * @email   714081644@qq.com
 */

data class FaceFeatureDataBean(
    /**
     * 泛型数据
     */
    private val bean: Any,
    /**
     * 人脸特征码二进制数据
     */
    val feature: ByteArray
) {
    @Suppress("UNCHECKED_CAST")
    fun <T> getBean(): T {
        return bean as T
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FaceFeatureDataBean

        if (bean != other.bean) return false
        if (!feature.contentEquals(other.feature)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bean.hashCode()
        result = 31 * result + feature.contentHashCode()
        return result
    }

}