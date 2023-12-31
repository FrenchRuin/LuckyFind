package com.example.luckyfind.config

import com.example.luckyfind.domain.repository.TokenRepository
import com.example.luckyfind.service.TokenService
import com.example.luckyfind.utils.CookieUtils
import com.example.luckyfind.utils.JWTUtils
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val authenticationConfiguration: AuthenticationConfiguration,
    private val jwtUtils: JWTUtils,
    private val cookieUtils: CookieUtils,
    private val tokenService: TokenService,
) {
    // Cors Filter Custom
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.allowedOrigins = listOf("*")
        config.allowedMethods = listOf("*")
        config.allowedHeaders = listOf("*")
        config.addExposedHeader("Authorization")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return source
    }

    // Filter Chain
    @Bean
    fun filterChain(
        http: HttpSecurity,
        jwtAuthorizationFilter: JwtAuthorizationFilter,
    ): SecurityFilterChain {
        http.cors {
            corsConfigurationSource()
        }
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
                defaultSuccessUrl("/index", false)
            }
            logout {
                logoutSuccessUrl = "/login"
            }
            authorizeHttpRequests {
                authorize("/swagger-ui/**", permitAll)
                authorize(PathRequest.toH2Console(), permitAll)
                authorize("/h2-console/**", permitAll)
                authorize("/assets/**", permitAll)
                authorize("/login", permitAll)
                authorize("/api/v1/**", permitAll)
                authorize("/api/v1/user/signUp", permitAll)
                authorize(HttpMethod.POST, "/api/v1/user/login", permitAll)
                authorize("/register", permitAll)
//                authorize("/index", permitAll)
                authorize(anyRequest, authenticated)
            }
            sessionManagement {
                // 세션을 사용하지 않고 JWT를 사용할 예정
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
        }
        http.addFilterBefore(
            jwtAuthenticationFilter(),
            UsernamePasswordAuthenticationFilter::class.java
        )
        http.addFilterBefore(
            jwtAuthorizationFilter,
            UsernamePasswordAuthenticationFilter::class.java
        )
        // JWT token
        return http.build()!!
    }


    @Bean
    fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
        val testFilter = JwtAuthenticationFilter(jwtUtils, cookieUtils, tokenService)
        testFilter.setAuthenticationManager(authenticationManager())
        return testFilter
    }

    @Bean
    fun authenticationManager(): AuthenticationManager {
        return this.authenticationConfiguration.authenticationManager
    }

    // Password Encoder
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}
