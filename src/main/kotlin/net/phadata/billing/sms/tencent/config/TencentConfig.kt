package net.phadata.billing.sms.tencent.config

import net.phadata.billing.sms.tencent.template.Template
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component


/**
 *
 * 腾讯sms配置
 * @author linx
 * @since 2022-08-02 16:28
 *
 */
@Component
@ConfigurationProperties(prefix = "tencent.sms")
class TencentConfig {
     var secretId: String? = null
     var secretKey: String? = null
     var connTimeout: Int? = null
     var reqMethod: String? = null
     var endPoint: String? = null
     var template: Template? = null


     override fun toString(): String {
          return "TencentConfig(secretId=$secretId, secretKey=$secretKey, connTimeout=$connTimeout, reqMethod=$reqMethod, endPoint=$endPoint, template=$template)"
     }
}