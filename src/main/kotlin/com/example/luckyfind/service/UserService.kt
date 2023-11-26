package com.example.luckyfind.service

import com.example.luckyfind.domain.entity.User
import com.example.luckyfind.domain.entity.UserAuthority
import com.example.luckyfind.domain.enum.AuthorityType
import com.example.luckyfind.domain.repository.UserRepository
import com.example.luckyfind.exception.UserNotFoundException
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

    ) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails =
        userRepository.findByUsername(username) ?: throw UserNotFoundException("유저가 존재하지 않습니다.")


    @Transactional(readOnly = true)
    fun findUser(username: String): UserResponse =
        UserResponse(userRepository.findByUsername(username) ?: throw UserNotFoundException("유저가 존재하지 않습니다."))


    // 회원가입
    @Transactional
    fun signUp(request: UserRequest): UserResponse {
        val user = userRepository.save(
            User(
                username = request.username,
                password = passwordEncoder.encode(request.password),
                enabled = true,
            )
        )
        //권한 부여
        addAuthority(user.userId!!, request.authority)

        return UserResponse(user)
    }

    // 권한 부여
    fun addAuthority(userId: Long, authority: String) {
        userRepository.findById(userId).ifPresent {
            val newAuthority = UserAuthority(userId, it, AuthorityType(authority))
            if (it.authorities == null) {
                val authoritySet = mutableSetOf(newAuthority)
                it.authorities = authoritySet
                userRepository.save(it)
            }
        }
    }
}


