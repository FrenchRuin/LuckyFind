package com.example.luckyfind.config

import com.example.luckyfind.service.UserService
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
) {

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
//                loginProcessingUrl = "/api/v1/user/login"
                defaultSuccessUrl("/index", false)
                permitAll()
            }

            authorizeHttpRequests {
                authorize("/swagger-ui/*", permitAll)
                authorize(PathRequest.toH2Console(), permitAll)
                authorize("/h2-console/**", permitAll)
                authorize("/assets/**", permitAll)
                authorize("/login", permitAll)
                authorize("/api/v1/user/login", permitAll)
                authorize("/api/v1/user/signUp", permitAll)
                authorize("/register", permitAll)
                authorize(anyRequest, authenticated)
            }
            sessionManagement {
                // 세션을 사용하지 않고 JWT를 사용할 예정
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
        }
        // JWT token
        http
            .addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter::class.java)
        return http.build()!!
    }

    // 비밀번호 암호화
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}
