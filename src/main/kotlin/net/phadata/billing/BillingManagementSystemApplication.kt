package net.phadata.billing

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication
class BillingManagementSystemApplication
fun main(args: Array<String>) {
    runApplication<BillingManagementSystemApplication>(*args)
}
