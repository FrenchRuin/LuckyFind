package com.example.luckyfind.config

import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    // 기본 filterchain  >> DSL 형식
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.invoke {
            csrf {
                disable()
            }
            headers {
                frameOptions {
                    sameOrigin = true
                }
            }
            formLogin {
                loginPage = "/login"
                loginProcessingUrl = "/login-process"
                defaultSuccessUrl("/index", false)
                permitAll()
            }

            authorizeHttpRequests {
                authorize("/swagger-ui/*", permitAll)
                authorize(PathRequest.toH2Console(), permitAll)
                authorize("/h2-console/**", permitAll)
                authorize("/assets/**", permitAll)
                authorize("/login", permitAll)
                authorize("/api/v1/user/signUp", permitAll)
                authorize("/register", permitAll)
                authorize(anyRequest, authenticated)
            }
//            sessionManagement {
//                sessionCreationPolicy = SessionCreationPolicy.STATELESS
//            }
        }
        return http.build()!!
    }

    // 비밀번호 암호화
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}
