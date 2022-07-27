package net.phadata.billing.utils

import java.nio.charset.StandardCharsets
import java.time.Instant
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


/**
 * 鉴权中心util
 * @author linx
 * @since 2022-07-25 15:58
 *
 */
object AuthCenterUtil {
    //鉴权中心的token
    const val TOKEN: String = "token"
    private const val BASE = "abcdefghijklmnopqrstuvwxyz0123456789"
    fun getSign(appKey: String?, appSecret: String, rand: String?, timestamp: String?): String {
        // 有随机数的签名
        //val raw = "appKey=%s&appSecret=%s&rand=%s&timestamp=%s"
        // val plain = String.format(raw, appKey, appSecret, rand, timestamp)
        val plain = "appKey=${appKey}&appSecret=${appSecret}&rand=${rand}&timestamp=${timestamp}"
        val secretKeySpec = SecretKeySpec(appSecret.toByteArray(StandardCharsets.UTF_8), "HmacSHA256")
        var mac: Mac? = null
        try {
            mac = Mac.getInstance("HmacSHA256")
            mac.init(secretKeySpec)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val bytes = mac!!.doFinal(plain.toByteArray())
        return byte2HexString(bytes)
    }

    /**
     * 随机生成4-6 位字符串
     */
    fun randStr(): String {
        val sb = StringBuilder()
        val length = (Math.random() * 3 + 4).toInt()
        for (i in 0 until length) {
            val number = (Math.random() * BASE.length).toInt()
            sb.append(BASE[number])
        }
        return sb.toString()
    }

    /**
     * 获取当前时间戳
     *
     * @return 时间戳字符串
     */
    fun timestampStr(): String {
        val second = Instant.now().epochSecond
        return second.toString()
    }

    private fun byte2HexString(bytes: ByteArray): String {
        val stringBuffer = StringBuilder()
        for (i in bytes.indices) {
            val temp = Integer.toHexString(bytes[i].toInt() and 255)
            if (temp.length == 1) {
                stringBuffer.append("0")
            }
            stringBuffer.append(temp)
        }
        return stringBuffer.toString()
    }
}