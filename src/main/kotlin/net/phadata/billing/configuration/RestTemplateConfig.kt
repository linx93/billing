package net.phadata.billing.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate


/**
 * RestTemplate配置
 * @author linx
 * @since 2022-07-21 11:38
 *
 */
@Configuration
class RestTemplateConfig {
    /**
     * 使用spring默认客户端SimpleClientHttpRequestFactory
     */
    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }

    /**
     * 使用OKHttp客户端（OkHttp3ClientHttpRequestFactory）。
     *     @Bean
    @DependsOn("okHttpClient")
    public RestTemplate restTemplate4(OkHttpClient okHttpClient) {
    RestTemplate restTemplate = new RestTemplate(okHttp3ClientHttpRequestFactory(okHttpClient));
    setRestTemplate(restTemplate);
    return restTemplate;
    }

    @Bean
    public ClientHttpRequestFactory okHttp3ClientHttpRequestFactory(OkHttpClient okHttpClient) {
    return new OkHttp3ClientHttpRequestFactory(okHttpClient);
    }
     */


}