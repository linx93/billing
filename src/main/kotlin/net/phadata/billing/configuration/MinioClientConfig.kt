package net.phadata.billing.configuration

import io.minio.MinioClient
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component


/**
 * minio配置
 * @author linx
 * @since 2022-07-20 17:08
 *
 */
@Component
@ConfigurationProperties(prefix = "minio")
class MinioClientConfig {
    var endpoint: String? = null
    var accessKey: String? = null
    var secretKey: String? = null
    var bucketName: String? = null

    /**
     * 注入minio 客户端
     *
     * @return MinioClient
     */
    @Bean
    fun minioClient(): MinioClient {
        return MinioClient.builder()
            .endpoint(endpoint)
            .credentials(accessKey, secretKey)
            .build()
    }
}