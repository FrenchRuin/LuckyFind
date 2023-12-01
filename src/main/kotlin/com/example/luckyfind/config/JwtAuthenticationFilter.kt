package com.example.luckyfind.config

import com.example.luckyfind.domain.entity.User
import com.example.luckyfind.domain.entity.UserAuthority
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Order(0)
@Component
class JwtAuthenticationFilter(
    private val tokenProvider: TokenProvider,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = parseBearerToken(request)
        println(token)
        val user = parseUserSpecification(token)
        println(user)
        UsernamePasswordAuthenticationToken(user, token, user.authorities)
            .apply { details = WebAuthenticationDetails(request) }
            .also { SecurityContextHolder.getContext().authentication = it }

        filterChain.doFilter(request, response)
    }


    private fun parseBearerToken(request: HttpServletRequest): String {
        return request.getHeader(HttpHeaders.AUTHORIZATION)
            .takeIf { it?.startsWith("Bearer ", true) ?: false }?.substring(7).toString()

    }

    private fun parseUserSpecification(token: String?) = (
            token?.takeIf { it.length >= 10 }
                ?.let { tokenProvider.validateTokenAndGetSubject(it) }
                ?: "anonymous:anonymous"
            ).split(":")
        .let { User(null, it[0], BCryptPasswordEncoder().encode("1234"), true) }
}
