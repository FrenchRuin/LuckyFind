package com.example.luckyfind.config

import com.example.luckyfind.domain.repository.UserRepository
import com.example.luckyfind.exception.UserNotFoundException
import com.example.luckyfind.utils.JWTUtils
import jakarta.servlet.FilterChain
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class JwtAuthenticationFilter(
    private val jwtUtils: JWTUtils,
    private val userRepository: UserRepository,
) : UsernamePasswordAuthenticationFilter() {

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        // usernamePasswordToken
        val authenticationToken = UsernamePasswordAuthenticationToken(
            request?.getParameter(this.usernameParameter),
            request?.getParameter(this.passwordParameter)
        )
        return authenticationManager.authenticate(authenticationToken) // 로그인 시도
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?
    ) {


        super.getSuccessHandler().onAuthenticationSuccess(request, response, authResult)
    }
}
