package net.phadata.billing.network

import net.phadata.billing.utils.AssertUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate


/**
 * 基础实现
 * @author linx
 * @since 2022-07-21 09:41
 *
 */
//@Component
abstract class BaseServerApi {
    @Autowired
    lateinit var restTemplate: RestTemplate

    /**
     * 构建完整请求地址
     *
     * @param address 地址
     * @param url     一定不是/开头的
     * @return
     */
    fun buildUrl(address: String, url: String): String {
        AssertUtil.notBlank(address, "address不能为空")
        val str = "/"
        if (url.startsWith(str)) url.replaceFirst(str, "")
        return if (address.endsWith(str)) {
            address + url
        } else address + str + url
    }
}