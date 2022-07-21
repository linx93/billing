package net.phadata.billing

import net.phadata.billing.model.billing.NotifyBillingRequest
import net.phadata.billing.network.BillingServerApi
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


/**
 * 网络测试
 * @author linx
 * @since 2022-07-21 11:50
 *
 */
@SpringBootTest
class NetworkTest {
    @Autowired
    lateinit var billingServerApi: BillingServerApi

    @Test
    fun test() {
        billingServerApi.notifyBilling(NotifyBillingRequest().apply {
            ""
            2
            ""
        })
    }
}