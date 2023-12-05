package com.example.luckyfind.config

import com.example.luckyfind.utils.JWTUtils
import io.swagger.v3.oas.models.responses.ApiResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationSuccessHandler(
    private val jwtUtils: JWTUtils,
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        SecurityContextHolder.getContext().authentication = authentication
        val token = jwtUtils.generateToken(authentication!!)
        response?.setHeader("Authorization", "Bearer $token")
    }
}