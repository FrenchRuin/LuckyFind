package com.example.luckyfind

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class LuckyFindApplication

fun main(args: Array<String>) {
    SpringApplication.run(LuckyFindApplication::class.java,  *args)
}
