package com.example.luckyfind.service

import com.example.luckyfind.domain.entity.User
import com.example.luckyfind.domain.enum.AuthorityType
import com.example.luckyfind.domain.repository.UserRepository
import com.example.luckyfind.exception.UserNotFoundException
import com.example.luckyfind.model.LogInRequest
import com.example.luckyfind.model.LogInResponse
import com.example.luckyfind.model.UserRequest
import com.example.luckyfind.model.UserResponse
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        println("loadUserByUsername")
        return userRepository.findByUsername(username) ?: throw UserNotFoundException("유저가 존재하지 않습니다.")
    }


    // 회원가입
    @Transactional
    fun signUp(request: UserRequest): UserResponse {
        val user = userRepository.findByUsername(request.username) ?: userRepository.save(
            User(
                username = request.username,
                password = passwordEncoder.encode(request.password),
                enabled = true,
                authority = AuthorityType.ROLE_USER
            )
        )
        return UserResponse(user)
    }

    @Transactional
    fun login(request: LogInRequest, httpServletResponse: HttpServletResponse) {
        println(request)
        println("로그인수행")
        val cookie = Cookie("refreshToken", "ddddd")
        cookie.secure = true
        cookie.isHttpOnly = true
        httpServletResponse.addCookie(cookie)
        httpServletResponse.setHeader("authorization", "ddddd")
        println("로그인끝")
        httpServletResponse.sendRedirect("/index")
    }


}


