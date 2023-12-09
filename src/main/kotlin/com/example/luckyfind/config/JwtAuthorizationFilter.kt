package com.example.luckyfind.config

import com.example.luckyfind.domain.repository.UserRepository
import com.example.luckyfind.utils.CookieUtils
import com.example.luckyfind.utils.JWTUtils
import jakarta.servlet.FilterChain
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.util.Date

class JwtAuthorizationFilter(
    private val userRepository: UserRepository,
    private val jwtUtils: JWTUtils,
    private val cookieUtils: CookieUtils,
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

        // TODO
        // OncePerRequestFilter 두번 호출됨 수정 필요 2023.12.09
        println(request.servletPath) // request Path Print Test

        // RefreshToken in Cookie
        val refreshToken = cookieUtils.getCookieValue(request.cookies, "refreshToken")
        if (jwtUtils.isExpired(refreshToken)) { // if refreshToken expired then
            response.sendRedirect("/logout") // logout
            val accessToken = request.getHeader("Authorization").split(" ")[1]
            println(accessToken)
            val authentication = jwtUtils.getAuthentication(accessToken) // UsernamePasswordAuthenticationToken return
            println(authentication)
            SecurityContextHolder.getContext().authentication = authentication
            filterChain.doFilter(request, response)
            return
        }



        val user = userRepository.findByUsername("admin")
        val authentication: Authentication = UsernamePasswordAuthenticationToken(
            user, user!!.password, user.authorities
        )

        val token = jwtUtils.generateToken(authentication)
        SecurityContextHolder.getContext().authentication = authentication
        response.addCookie(cookieUtils.createCookie("refreshToken", token.refreshToken)) // refreshToken put in Cookie
        response.setHeader("Authorization", "Bearer ${token.accessToken}") // accessToken put in ResponseHeader
        filterChain.doFilter(request, response)
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return excludeUrls.stream().anyMatch {
            request.servletPath.contains(it)
        }
    }
}