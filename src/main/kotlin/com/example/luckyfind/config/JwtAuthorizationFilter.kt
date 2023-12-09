package com.example.luckyfind.config

import com.example.luckyfind.domain.repository.UserRepository
import com.example.luckyfind.utils.JWTUtils
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthorizationFilter(
    private val userRepository: UserRepository,
    private val jwtUtils: JWTUtils,
) : OncePerRequestFilter() {

    // 요청 제외 url
    private val excludeUrls =
        listOf(
            "/assets",
            "/login",
            "/templates",
            "/scripts",
            "/swagger-ui/",
//            "/v3/api-docs"
        )

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        println(request.servletPath) // 요청 path

        val header = request.getHeader("Authorization")
        println(header)
//        if (header == null || !header.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response)
//            return
//        }

        val user = userRepository.findByUsername("admin")
        val authentication: Authentication = UsernamePasswordAuthenticationToken(
            user, user!!.password, user.authorities
        )

        val token = jwtUtils.generateToken(authentication)

        SecurityContextHolder.getContext().authentication = authentication

        response.setHeader("Authorization", "Bearer ${token.accessToken}")
        filterChain.doFilter(request, response)
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return excludeUrls.stream().anyMatch {
            request.servletPath.contains(it)
        }
    }
}