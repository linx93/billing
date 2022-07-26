package net.phadata.billing

import net.phadata.billing.model.po.OrderRecords
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.task.SimpleAsyncTaskExecutor

@SpringBootTest
class BillingManagementSystemApplicationTests {


	@Test
	fun contextLoads() {
		println("simpleAsyncTaskExecutor")
		val orderRecords = OrderRecords()
		println(orderRecords)
	}

}
