package net.phadata.billing.configuration

import com.baomidou.mybatisplus.annotation.DbType
import com.baomidou.mybatisplus.core.toolkit.Sequence
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor
import net.phadata.billing.interceptor.TokenInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


/**
 *
 * @author linx
 * @since 2022-06-14 00:31
 *
 */
@Configuration
class ApplicationConfig : WebMvcConfigurer {
    /**
     * 注册自定义拦截器
     */
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(TokenInterceptor())
            .addPathPatterns("/api/v1/**")
            .excludePathPatterns(
                "/doc.html",
                "/favicon.ico",
                "/webjars/**",
                "/swagger-resources/**",
                "/api/v1/sys-user/login",
            )
    }

    /**
     * mybatis-plus的分页配置
     *
     * @return
     */
    @Bean
    fun mybatisPlusInterceptor(): MybatisPlusInterceptor {
        val interceptor = MybatisPlusInterceptor()
        interceptor.addInnerInterceptor(PaginationInnerInterceptor(DbType.MYSQL))
        return interceptor
    }

    /**
     * 生成Sequence 雪花算法
     *
     * @return
     */
    @Bean
    fun sequence(): Sequence {
        return Sequence()
    }
}
