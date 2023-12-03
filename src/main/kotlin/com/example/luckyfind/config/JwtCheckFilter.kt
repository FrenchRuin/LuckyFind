package com.example.luckyfind.config

import com.example.luckyfind.domain.repository.UserRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter


class JwtCheckFilter(
    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
)  : BasicAuthenticationFilter(authenticationManager)  {


    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {

//        // 1. 요청온 header 확인
//        val header = request.getHeader("Authorization ")
//
//        if (header == null || !header.startsWith("Bearer ")) {
//            chain.doFilter(request, response)
//            return
//        }
        val user = userRepository.findByUsername("admin")
        val authentication : Authentication = UsernamePasswordAuthenticationToken(
            user, user!!.password, user.authorities
        )
        SecurityContextHolder.getContext().authentication = authentication


//        // 2. JWT 토큰을 검증해서 정상적인 사용자인지 확인
//        String jwtToken = request.getHeader(JwtProperties.HEADER_STRING)
//            .replace(JwtProperties.TOKEN_PREFIX, "");
//
//        String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(jwtToken)
//            .getClaim("username").asString();
//
//        if (username != null) {
//            User userEntity = userRepository.findByUsername(username);
//
//            CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);
//
//            // Authentication 객체를 강제로 만듦 (vs. JwtAuthenticationFilter에선 로그인을 해서 만들어진 것)
//            Authentication authentication = new UsernamePasswordAuthenticationToken(
//                customUserDetails,
//                userEntity.getPassword(), // 패스워드
//                customUserDetails.getAuthorities()); // 권한
//
//            // 시큐리티의 세션 영역에 접근하여 Authentication 객체 저장
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
        chain.doFilter(request, response);
    }
}