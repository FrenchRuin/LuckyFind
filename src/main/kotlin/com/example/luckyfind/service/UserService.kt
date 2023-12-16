package com.example.luckyfind.service

import com.example.luckyfind.config.TokenProvider
import com.example.luckyfind.domain.entity.User
import com.example.luckyfind.domain.enum.AuthorityType
import com.example.luckyfind.domain.repository.UserRepository
import com.example.luckyfind.exception.UserNotFoundException
import com.example.luckyfind.model.LogInResponse
import com.example.luckyfind.model.UserRequest
import com.example.luckyfind.model.UserResponse
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val tokenProvider: TokenProvider,
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails =
        userRepository.findByUsername(username) ?: throw UserNotFoundException("유저가 존재하지 않습니다.")


    @Transactional(readOnly = true)
    fun findUser(username: String): UserResponse =
        UserResponse(userRepository.findByUsername(username) ?: throw UserNotFoundException("유저가 존재하지 않습니다."))

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
    fun login(request: UserRequest): LogInResponse {
        userRepository.findByUsername(request.username)?.takeIf {
            passwordEncoder.matches(request.password, it.password)
        } ?: throw UserNotFoundException("유저가 존재하지 않습니다.")
        val token = tokenProvider.createToken(request.username + ":ROLE_USER")
        println(token)
        return LogInResponse(
            request.username,
            token,
        )
    }

}


