package com.example.luckyfind.config

import com.example.luckyfind.utils.JwtUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.GenericFilter
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val accessToken = jwtUtil.resolveToken(request)
        println("token :: $accessToken")
        if (accessToken == null) {
            filterChain.doFilter(request,response)
            return
        }


//        jwtUtil.resolveClaims(request)
    }
}