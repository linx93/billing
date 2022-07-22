package net.phadata.billing

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

@EnableAsync
@EnableScheduling
@SpringBootApplication
class BillingManagementSystemApplication
fun main(args: Array<String>) {
    runApplication<BillingManagementSystemApplication>(*args)
}
