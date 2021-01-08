package com.welfare.servicemerchant.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.welfare.common.constants.WelfareConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author liyx
 * @date 12/16/2020
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
public class Swagger2Config {
    @Bean
    public Docket createRestApi() {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new ParameterBuilder()
                .name(WelfareConstant.Header.SOURCE.code())
                .description("请求来源,需要找e-welfare申请")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(true)
                .build());
        parameters.add(new ParameterBuilder()
                .name(WelfareConstant.Header.MERCHANT_USER.code())
                .description("商户请求用户信息")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build());
        return  new Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(parameters)
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.welfare.servicemerchant.controller"))
                .paths(PathSelectors.any())
                .build();

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("swagger-bootstrap-ui RESTful APIs")
                .description("swagger-bootstrap-ui")
                .termsOfServiceUrl("http://localhost:8999/")
                .version("1.0")
                .build();
    }
}
