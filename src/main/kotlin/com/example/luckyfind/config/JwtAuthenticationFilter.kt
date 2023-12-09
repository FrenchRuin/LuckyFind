package com.example.luckyfind.config

import com.example.luckyfind.utils.JWTUtils
import jakarta.servlet.FilterChain
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class JwtAuthenticationFilter(
    private val jwtUtils: JWTUtils,
) : UsernamePasswordAuthenticationFilter() {

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        // username && password parameter
        println(request?.getParameter(this.usernameParameter))
        println(request?.getParameter(this.passwordParameter))

        val authenticationToken =
            UsernamePasswordAuthenticationToken(
                request?.getParameter(this.usernameParameter),
                request?.getParameter(this.passwordParameter)
            )
        println(authenticationToken)                // authentication Token
        println(authenticationToken.principal)      // username
        println(authenticationToken.credentials)    // password

        println("로그인 시도 ==========================")
        val authentication: Authentication = authenticationManager.authenticate(authenticationToken) // 로그인 시도
        println(authentication.isAuthenticated)
        println("로그인 시도 종료 ==========================")
        // Authentication return
        return authentication
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?
    ) {
        println("로그인성공 메소드 시작==============================")
        // authResult
        println(authResult?.principal)
        println(authResult?.credentials)

        // token model //
        val token = jwtUtils.generateToken(authResult!!)
        println(token)

        val cookie = Cookie("refreshToken", token.refreshToken)
        cookie.isHttpOnly = true // prevent access Browser to find refreshToken
        cookie.secure = true    // only https
        response?.addCookie(cookie) // refresh Token in Cookie
        response?.setHeader("Authorization", "Bearer ${token.accessToken}") // accessToken in Header

        println("로그인성공 메소드 종료 ====================")

        super.successfulAuthentication(request, response, chain, authResult)
    }
}
