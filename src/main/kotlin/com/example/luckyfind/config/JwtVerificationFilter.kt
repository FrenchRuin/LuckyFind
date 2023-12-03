package com.example.luckyfind.config

import com.example.luckyfind.utils.JWTUtils
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.filter.OncePerRequestFilter
import org.thymeleaf.util.StringUtils

class JwtVerificationFilter(
    private val jwtUtils: JWTUtils,
) : OncePerRequestFilter() {

    private val excludeUrls =
        listOf(
            "/static/**",
            "/templates/**",
            "/scripts/**",
            "/login",
            "/assets"
        )


    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        println("OncePerRequestFilter ::: ${request.requestURI}")

        filterChain.doFilter(request, response)
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return excludeUrls.stream().anyMatch {
            request.servletPath.contains(it)
        }
    }
}