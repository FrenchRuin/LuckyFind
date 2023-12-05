package com.example.luckyfind.config

import com.example.luckyfind.domain.entity.User
import com.example.luckyfind.model.UserResponse
import com.example.luckyfind.utils.JWTUtils
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.filter.GenericFilterBean

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

    // 인증성공
    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?
    ) {
        println("인증 완료 ==============")
        val userResponse: UserResponse = with((authResult?.principal as User)) {
            UserResponse(
                username = this.username,
                password = this.password,
                authorities = this.authorities!!,
                userId = this.userId!!,
                enabled = this.enabled
            )
        }
        println(userResponse) // 인증정보
        val token = jwtUtils.createToken(userResponse)
        println(token)

        SecurityContextHolder.getContext().authentication = authResult
        // 토큰 헤더에 추가
        response?.addHeader("Authorization", "Bearer $token")
//        response?.sendRedirect("/notice")
        super.successfulAuthentication(request, response, chain, authResult)
    }

    override fun unsuccessfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        failed: AuthenticationException?
    ) {
        println("실패??")
        super.unsuccessfulAuthentication(request, response, failed)
    }

}
