package com.example.luckyfind.config

import com.example.luckyfind.domain.repository.UserRepository
import com.example.luckyfind.service.UserService
import com.example.luckyfind.utils.CookieUtils
import com.example.luckyfind.utils.JWTUtils
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthorizationFilter(
    private val jwtUtils: JWTUtils,
    private val cookieUtils: CookieUtils,
    private val userService: UserService,
) : OncePerRequestFilter() {

    // 요청 제외 url
    private val excludeUrls =
        listOf(
            "/assets",
            "/login",
            "/templates",
            "/scripts",
            "/swagger-ui/",
//            "/index",
//            "/notice"
        )

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // TODO
        // OncePerRequestFilter 두번 호출됨 수정 필요 2023.12.09
        // 처음에 로그인 수행시, RefreshToken 으로 파악을하자.
        //
        println("OncePerRequestFilter")

//        println(request.servletPath) // request Path Print Test
        val refreshToken = cookieUtils.getRefreshTokenInCookie(request.cookies) // RefreshToken in Cookie
        val accessToken: String? = request.getHeader("Authorization") // AccessToken in RequestHeader
        println(refreshToken)
        println(accessToken)

        // case 1 : AccessToken & RefreshToken is Expired
        println("CASE 1")
        if ((jwtUtils.isExpired(refreshToken))) {
            filterChain.doFilter(request, response)
            return
        }

        // case 2 : AccessToken is Expired & RefreshToken is Valid
        println("CASE 2")
        if (jwtUtils.isExpired(accessToken?.split(" ")?.get(1))) {
            println("ACCESSTOKEN EXPIRED")
            val claims = jwtUtils.parseClaims(refreshToken)
            println(claims)
            val user = userService.loadUserByUsername(claims.subject)
            val newAccessToken = jwtUtils.generateAccessToken(claims)
            response.setHeader("Authorization", "Bearer $newAccessToken") // accessToken put in ResponseHeader
            SecurityContextHolder.getContext().authentication =
                UsernamePasswordAuthenticationToken(user, "", user.authorities)
            filterChain.doFilter(request, response)
            return
        }

        // case 3. RefreshToken is Valid & AccessToken is Valid
        val claims = jwtUtils.parseClaims(refreshToken)
        val user = userService.loadUserByUsername(claims.subject)
        response.setHeader("Authorization", "Bearer $accessToken") // accessToken put in ResponseHeader
        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(user, "", user.authorities)
        filterChain.doFilter(request, response)
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return excludeUrls.stream().anyMatch {
            request.servletPath.contains(it)
        }
    }
}