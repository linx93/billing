package net.phadata.billing.sms.tencent.impl

import com.tencentcloudapi.common.Credential
import com.tencentcloudapi.common.exception.TencentCloudSDKException
import com.tencentcloudapi.common.profile.ClientProfile
import com.tencentcloudapi.common.profile.HttpProfile
import com.tencentcloudapi.sms.v20210111.SmsClient
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse
import net.phadata.billing.constant.ResultCode
import net.phadata.billing.exception.ServiceException
import net.phadata.billing.sms.tencent.TencentSmsService
import net.phadata.billing.sms.tencent.config.TencentConfig
import net.phadata.billing.sms.tencent.template.BaseTemplate
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component


/**
 * 实现
 * @author linx
 * @since 2022-08-02 17:02
 *
 */
@Component
class TencentSmsServiceImpl : TencentSmsService {
    private val log = LoggerFactory.getLogger(TencentSmsServiceImpl::class.java)

    var DEFAULT_CONN_TIMEOUT = 60
    var DEFAULT_REQ_METHOD = HttpMethod.POST.name
    var DEFAULT_ENDPOINT = "sms.tencentcloudapi.com"
    var DEFAULT_COUNTRY_CODE = "+86"


    private val SIGN_NAME = "天机信息"

    /**
     * SDK_APP_ID:个人可信数字账户平台
     */
    private val SDK_APP_ID = "1400703951"

    @Autowired
    private lateinit var tencentConfig: TencentConfig

    override fun sendSms(
        phoneNumbers: List<String>,
        templateParams: List<String>,
        template: BaseTemplate
    ): SendSmsResponse? {
        try {
            /* 必要步骤：
             * 实例化一个认证对象，入参需要传入腾讯云账户密钥对secretId，secretKey。
             * 这里采用的是从环境变量读取的方式，需要在环境变量中先设置这两个值。
             * 你也可以直接在代码中写死密钥对，但是小心不要将代码复制、上传或者分享给他人，
             * 以免泄露密钥对危及你的财产安全。
             * SecretId、SecretKey 查询: https://console.cloud.tencent.com/cam/capi
             */
            val cred = Credential(tencentConfig.secretId, tencentConfig.secretKey)

            // 实例化一个http选项，可选，没有特殊需求可以跳过
            val httpProfile = HttpProfile()
            // 设置代理（无需要直接忽略）
            // httpProfile.setProxyHost("真实代理ip");
            // httpProfile.setProxyPort(真实代理端口);
            /*
             * SDK默认使用POST方法。
             * 如果你一定要使用GET方法，可以在这里设置。GET方法无法处理一些较大的请求
             */httpProfile.reqMethod = getReqMethod()
            /*
             * SDK有默认的超时时间，非必要请不要进行调整
             * 如有需要请在代码中查阅以获取最新的默认值
             */httpProfile.connTimeout = getConnTimeout()
            /* 指定接入地域域名，默认就近地域接入域名为 sms.tencentcloudapi.com ，也支持指定地域域名访问，例如广州地域的域名为 sms.ap-guangzhou.tencentcloudapi.com */httpProfile.endpoint =
                getEndPoint()

            /*
             * 非必要步骤:
             * 实例化一个客户端配置对象，可以指定超时时间等配置
             */
            val clientProfile = ClientProfile()
            /*
             * SDK默认用TC3-HMAC-SHA256进行签名
             * 非必要请不要修改这个字段
             */
            //clientProfile.setSignMethod("HmacSHA256");
            clientProfile.httpProfile = httpProfile
            /* 实例化要请求产品(以sms为例)的client对象
             * 第二个参数是地域信息，可以直接填写字符串ap-guangzhou，支持的地域列表参考 https://cloud.tencent.com/document/api/382/52071#.E5.9C.B0.E5.9F.9F.E5.88.97.E8.A1.A8
             */
            val client = SmsClient(cred, "ap-guangzhou", clientProfile)
            /* 实例化一个请求对象，根据调用的接口和实际情况，可以进一步设置请求参数
             * 你可以直接查询SDK源码确定接口有哪些属性可以设置
             * 属性可能是基本类型，也可能引用了另一个数据结构
             * 推荐使用IDE进行开发，可以方便的跳转查阅各个接口和数据结构的文档说明
             */
            val req = SendSmsRequest()

            /* 填充请求参数,这里request对象的成员变量即对应接口的入参
             * 你可以通过官网接口文档或跳转到request对象的定义处查看请求参数的定义
             * 基本类型的设置:
             * 帮助链接：
             * 短信控制台: https://console.cloud.tencent.com/smsv2
             * 腾讯云短信小助手: https://cloud.tencent.com/document/product/382/3773#.E6.8A.80.E6.9C.AF.E4.BA.A4.E6.B5.81
             */

            /* 短信应用ID: 短信SdkAppId在 [短信控制台] 添加应用后生成的实际SdkAppId，示例如1400006666 */
            // 应用 ID 可前往 [短信控制台](https://console.cloud.tencent.com/smsv2/app-manage) 查看
            req.smsSdkAppId = SDK_APP_ID

            /* 短信签名内容: 使用 UTF-8 编码，必须填写已审核通过的签名 */
            // 签名信息可前往 [国内短信](https://console.cloud.tencent.com/smsv2/csms-sign) 或 [国际/港澳台短信](https://console.cloud.tencent.com/smsv2/isms-sign) 的签名管理查看
            req.signName = SIGN_NAME

            /* 模板 ID: 必须填写已审核通过的模板 ID */
            // 模板 ID 可前往 [国内短信](https://console.cloud.tencent.com/smsv2/csms-template) 或 [国际/港澳台短信](https://console.cloud.tencent.com/smsv2/isms-template) 的正文模板管理查看
            req.templateId = template.id

            /* 模板参数: 模板参数的个数需要与 TemplateId 对应模板的变量个数保持一致，若无模板参数，则设置为空 */
            //您的平台验证码：{1}，请于{2}分钟内填写，请勿泄露。

            //String[] templateParamSet = {"666666", "5"};
            val smsContent = buildSmsContent(templateParams, template)
            log.info("短信内容:{}", smsContent)
            req.templateParamSet = templateParams.toTypedArray()

            /* 下发手机号码，采用 E.164 标准，+[国家或地区码][手机号]
             * 示例如：+8613711112222， 其中前面有一个+号 ，86为国家码，13711112222为手机号，最多不要超过200个手机号 */
            //String[] phoneNumberSet = {"+8618625779553"};
            val phoneNumbers = getPhoneNumbers(phoneNumbers)
            phoneNumbers.forEach {
                log.info("phone==:$it")
            }
            req.phoneNumberSet = phoneNumbers


            /* 用户的 session 内容（无需要可忽略）: 可以携带用户侧 ID 等上下文信息，server 会原样返回 */
            //String sessionContext = "linx";
            //req.setSessionContext(sessionContext);

            /* 短信码号扩展号（无需要可忽略）: 默认未开通，如需开通请联系 [腾讯云短信小助手] */
            //String extendCode = "";
            //req.setExtendCode(extendCode);

            /* 国际/港澳台短信 SenderId（无需要可忽略）: 国内短信填空，默认未开通，如需开通请联系 [腾讯云短信小助手] */
            //String senderid = "";
            //req.setSenderId(senderid);

            /*
             * 通过 client 对象调用 SendSms 方法发起请求。注意请求方法名与请求对象是对应的
             * 返回的 res 是一个 SendSmsResponse 类的实例，与请求对象对应
             */
            val res = client.SendSms(req)

            // 输出json格式的字符串回包
            log.info("短信响应:{}", SendSmsResponse.toJsonString(res))
            return res
            // 也可以取出单个值，你可以通过官网接口文档或跳转到response对象的定义处查看返回字段的定义
        } catch (e: TencentCloudSDKException) {
            log.error("短信发送失败:{}", e.message)
        }
        return null
    }

    private fun getConnTimeout(): Int {
        val connTimeout: Int? = tencentConfig.connTimeout
        return if (connTimeout == null || connTimeout == 0) DEFAULT_CONN_TIMEOUT else connTimeout
    }

    private fun getReqMethod(): String? {
        val reqMethod: String? = tencentConfig.reqMethod
        return if (StringUtils.isBlank(reqMethod)) DEFAULT_REQ_METHOD else reqMethod
    }

    private fun getEndPoint(): String? {
        val endPoint: String? = tencentConfig.endPoint
        return if (StringUtils.isBlank(endPoint)) DEFAULT_ENDPOINT else endPoint
    }

    private fun getPhoneNumbers(phoneNumbers: List<String>): Array<String> {
        return phoneNumbers.map { element: String -> "$DEFAULT_COUNTRY_CODE$element" }.toTypedArray()
    }

    private fun buildSmsContent(templateParams: List<String>, template: BaseTemplate): String {
        var text: String = template.text
        if (templateParams.isEmpty()) {
            throw ServiceException(ResultCode.PARAMETER_VALIDATION_ERROR)
        }
        for (i in templateParams.indices) {
            text = text.replace("{" + (i + 1) + "}", templateParams[i])
        }
        return text
    }

}