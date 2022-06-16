package net.phadata.billing.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * Swagger配置
 *
 * @author linx
 * @since 2022-06-14 15:27
 */
@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {
    @Bean(value = "defaultApi")
    public Docket defaultApi2() {
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("")
                        .description("天机计费管理系统Api接口文档")
                        .termsOfServiceUrl("https://www.phadata.net")
                        .contact("xx@qq.com")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("2.X版本")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("net.phadata.billing.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}
