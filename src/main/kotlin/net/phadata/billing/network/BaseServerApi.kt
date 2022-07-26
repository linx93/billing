package net.phadata.billing.network

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import com.alibaba.fastjson.parser.Feature
import com.alibaba.fastjson.parser.ParserConfig
import net.phadata.billing.model.api.ApiResult
import net.phadata.billing.utils.AssertUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.client.RestTemplate
import java.lang.reflect.Type


/**
 * 基础实现
 * @author linx
 * @since 2022-07-21 09:41
 *
 */
//@Component
abstract class BaseServerApi {
    private val log = LoggerFactory.getLogger(BaseServerApi::class.java)

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

    fun <T> buildApiResult(responseStr: String?, type: Class<T>): ApiResult<T> {
        val apiResult: ApiResult<T> = JSONObject.parseObject(
            responseStr,
            object : TypeReference<ApiResult<T>>(type) {},
            *arrayOfNulls<Feature>(0)
        )
        log.info("响应apiResult:${apiResult}")
        return apiResult
        //请求异常的处理还需优化
        //return ApiResult<Any?>(ErrorCode.FAILED.code, ErrorCode.FAILED.name, response.body())
    }
}