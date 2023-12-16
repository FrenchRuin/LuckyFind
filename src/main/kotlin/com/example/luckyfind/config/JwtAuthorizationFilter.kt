package com.example.luckyfind.config

import com.example.luckyfind.domain.repository.UserRepository
import com.example.luckyfind.exception.CookieNotFoundException
import com.example.luckyfind.exception.TokenException
import com.example.luckyfind.exception.UserNotFoundException
import com.example.luckyfind.utils.CookieUtils
import com.example.luckyfind.utils.JWTUtils
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

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
            "/index",
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

        val header: String? = request.getHeader("Authorization")

//        if (header == null) {
//            filterChain.doFilter(request, response)
//        }
//        // RefreshToken in Cookie
//        val refreshToken = cookieUtils.getCookieValue(request.cookies, "refreshToken") ?: throw CookieNotFoundException(
//            "쿠키가 존재하지 않습니다."
//        )
//
//        if (jwtUtils.isExpired(refreshToken)) { // if refreshToken expired then
//            response.sendRedirect("/logout") // logout
//            val accessToken = request.getHeader("Authorization").split(" ")[1]
//            println(accessToken)
//            val authentication = jwtUtils.getAuthentication(accessToken) // UsernamePasswordAuthenticationToken return
//            println(authentication)
//            SecurityContextHolder.getContext().authentication = authentication
//            filterChain.doFilter(request, response)
//            return
//        }
//
//        // if refreshToken not expired then get userInfo in accessToken
//        var accessToken = request.getHeader("Authorization")
//        println(accessToken)
//        val claim = jwtUtils.parseClaims(accessToken) // parse Claims
        // username == claim.subject
        val user = userRepository.findByUsername("admin") ?: throw UserNotFoundException("유저가 존재하지 않습니다.")
        // authentication
        val authentication: Authentication = UsernamePasswordAuthenticationToken(
            user, user.password, user.authorities
        )

        println(authentication.authorities)

//        if (jwtUtils.isExpired(accessToken)) { // if accessToken expired then
//            accessToken = jwtUtils.generateAccessToken(claim)
//        }
        SecurityContextHolder.getContext().authentication = authentication
//        response.addCookie(cookieUtils.createCookie("refreshToken", refreshToken)) // refreshToken put in Cookie
//        response.setHeader("Authorization", "Bearer $accessToken") // accessToken put in ResponseHeader
        filterChain.doFilter(request, response)
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return excludeUrls.stream().anyMatch {
            request.servletPath.contains(it)
        }
    }
}