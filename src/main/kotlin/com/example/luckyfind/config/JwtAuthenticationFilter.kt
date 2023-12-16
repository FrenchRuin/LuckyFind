package com.example.luckyfind.config

import com.example.luckyfind.domain.entity.Token
import com.example.luckyfind.domain.entity.User
import com.example.luckyfind.domain.repository.TokenRepository
import com.example.luckyfind.model.TokenRequest
import com.example.luckyfind.service.TokenService
import com.example.luckyfind.utils.CookieUtils
import com.example.luckyfind.utils.JWTUtils
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class JwtAuthenticationFilter(
    private val jwtUtils: JWTUtils,
    private val cookieUtils: CookieUtils,
    private val tokenService: TokenService,
) : UsernamePasswordAuthenticationFilter() {

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        println("UsernamePasswordAuthenticationFilter")
        val username = request?.getParameter("username")
        val password = request?.getParameter("password")
        val token = UsernamePasswordAuthenticationToken(username, password)
        return authenticationManager.authenticate(token)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?
    ) {
        val token = jwtUtils.generateAccessToken(authResult!!)
        val cookie = cookieUtils.createCookie("refreshToken", token)
        response?.addCookie(cookie)
        response?.setHeader("Authorization", token)
        tokenService.addToken(Token(
            user = authResult.principal as User,
            token = token,
        ))
        chain?.doFilter(request, response)
    }
}