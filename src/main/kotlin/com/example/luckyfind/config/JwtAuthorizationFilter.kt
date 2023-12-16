package com.example.luckyfind.config

import com.example.luckyfind.domain.repository.UserRepository
import com.example.luckyfind.service.TokenService
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
    private val tokenService: TokenService,
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

        println(request.servletPath) // request Path Print Test
        val refreshToken = cookieUtils.getRefreshTokenInCookie(request.cookies) // RefreshToken in Cookie
        val accessToken: String? = request.getHeader("Authorization") // AccessToken in RequestHeader
        val claims = jwtUtils.parseClaims(refreshToken)
        val user = userService.loadUserByUsername(claims.subject) // user정보

        // case 0 : 토큰과 일치한지 체크
        val refreshTokenInDb = tokenService.getToken(user.username)
        println(refreshTokenInDb)
        if (!refreshToken.equals(refreshTokenInDb.token)) {
            tokenService.deleteToken(user.username)
            filterChain.doFilter(request, response)
            return
        }

        // case 1 : AccessToken & RefreshToken is Expired
        if ((jwtUtils.isExpired(refreshToken))) {
            tokenService.deleteToken(user.username)
            filterChain.doFilter(request, response)
            return
        }

        // case 2 : AccessToken is Expired & RefreshToken is Valid
        if (jwtUtils.isExpired(accessToken?.split(" ")?.get(1))) {
            val newAccessToken = jwtUtils.generateAccessToken(claims)
            response.setHeader("Authorization", "Bearer $newAccessToken") // accessToken put in ResponseHeader
            SecurityContextHolder.getContext().authentication =
                UsernamePasswordAuthenticationToken(user, "", user.authorities)
            filterChain.doFilter(request, response)
            return
        }

        // case 3. RefreshToken is Valid & AccessToken is Valid
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