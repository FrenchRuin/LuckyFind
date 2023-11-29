package com.example.luckyfind.config

import jakarta.servlet.FilterChain
import jakarta.servlet.GenericFilter
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils

class JwtAuthenticationFilter(
    private val tokenProvider: TokenProvider,
) : GenericFilter() {

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {

        val token = resolveToken(request as HttpServletRequest)

        if (token != null && tokenProvider.validateToken(token)) {
            val authentication = tokenProvider.getAuthentication(token)
            SecurityContextHolder.getContext().authentication = authentication

        }

        chain!!.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        return if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            bearerToken.substring(7)
        } else {
            null
        }
    }
}