package net.phadata.billing

import net.phadata.billing.sms.tencent.TencentSmsService
import net.phadata.billing.sms.tencent.config.TencentConfig
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


/**
 * 短信测试
 * @author linx
 * @since 2022-08-02 17:26
 *
 */
@SpringBootTest
class TencentSmsServiceTest {
    @Autowired
    private lateinit var tencentSmsService: TencentSmsService

    @Autowired
    private lateinit var tencentConfig: TencentConfig



    @Test
    fun sendSMS() {
        val invoiceNotice = tencentConfig.template?.invoiceNotice
        if (invoiceNotice != null) {
            tencentSmsService.sendSms(listOf("18798851389"), listOf("刘鹏宇", "个人"), invoiceNotice)
        }
    }
}