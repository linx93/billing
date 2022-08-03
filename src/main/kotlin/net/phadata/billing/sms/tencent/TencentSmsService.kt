package net.phadata.billing.sms.tencent

import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse
import net.phadata.billing.sms.tencent.template.BaseTemplate


/**
 *
 * @author linx
 * @since 2022-08-02 17:01
 *
 */
open interface TencentSmsService {
    /**
     * 发送短信
     *
     * @param phoneNumbers   电话号码
     * @param templateParams 模版参数
     * @return
     */
    fun sendSms(phoneNumbers: List<String>, templateParams: List<String>, template: BaseTemplate): SendSmsResponse?

}