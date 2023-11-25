package com.example.luckyfind.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun openApi(): OpenAPI = OpenAPI()
        .components(Components())
        .info(apiInfo())

    // Swagger UI API Info
    private fun apiInfo() = Info()
        .title("LuckyFind Test")
        .description("럭키 파인드 Rest API Test")
        .version("1.0.0")
}