package com.example.luckyfind.config

import com.example.luckyfind.domain.repository.UserAuthorityRepository
import com.example.luckyfind.domain.repository.UserRepository
import com.example.luckyfind.exception.UserNotFoundException
import com.example.luckyfind.utils.JWTUtils
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@Component
class JwtAuthenticationSuccessHandler(
    private val jwtUtils: JWTUtils,
    private val userRepository: UserRepository,
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        // token model //
        val token = jwtUtils.generateToken(authentication!!)

        val user = userRepository.findByUsername(authentication.name) ?: throw UserNotFoundException("유저가 존재하지 않습니다.")

        val authorities = mutableSetOf<GrantedAuthority>()
        user.getAuthorities().stream().forEach {
            authorities.add(SimpleGrantedAuthority(it.authority.toString()))
        }
        val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
            user, "", authorities
        )

        SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken

        println(usernamePasswordAuthenticationToken)

        val cookie = Cookie("refreshToken", token.refreshToken)
        cookie.isHttpOnly = true // prevent access Browser to find refreshToken
        cookie.secure = true    // only https
        response?.addCookie(cookie) // refresh Token in Cookie
        response?.setHeader("Authorization", "Bearer ${token.accessToken}") // accessToken in Header
        response?.sendRedirect("/index")
    }
}