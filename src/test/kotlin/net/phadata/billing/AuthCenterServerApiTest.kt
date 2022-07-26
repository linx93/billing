package net.phadata.billing

import net.phadata.billing.network.AuthCenterServerApi
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


/**
 * 鉴权中心接口测试
 * @author linx
 * @since 2022-07-25 17:42
 *
 */
@SpringBootTest
class AuthCenterServerApiTest {
    @Autowired
    lateinit var authCenterServerApi: AuthCenterServerApi

    @Test
    fun create() {
        val createToken = authCenterServerApi.createToken()
        println("createToken="+createToken)
    }

    @Test
    fun verify() {
        val verifyToken =
            authCenterServerApi.verifyToken("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcHBLZXkiOiJjYXNuY2FhODdkNXFrYXZhZXI2ZyIsImR0aWQiOiIiLCJleHAiOjE2NTg4NDc2NDMsImlhdCI6MTY1ODgwNDQ0MywiaXNzIjoi6LS15bee5aSp5py65L-h5oGv56eR5oqA5pyJ6ZmQ5YWs5Y-4IiwibmJmIjoxNjU4ODA0NDQzfQ.MEUCIQCwD985T6JzgoqNRJxQoUXnx9AcYCxhv/Z+vEqjfCbSxAIgHftahzD1AKcbKabqs11eZesxbJk8cYqqtSOhsgd1T8I=")
        println("verify:" + verifyToken)
    }
}