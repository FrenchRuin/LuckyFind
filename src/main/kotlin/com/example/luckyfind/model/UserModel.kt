package com.example.luckyfind.model

import com.example.luckyfind.domain.entity.User
import com.example.luckyfind.domain.entity.UserAuthority

// 로그인응답 response
data class LogInResponse(
    val username: String,
    val token: String,
)

data class UserRequest(
    val username: String,
    val password: String,
    val authority: String,
)

data class UserResponse(
    val userId: Long,
    val username: String,
    val password: String,
    val enabled: Boolean,
    val authorities: Set<UserAuthority>,
) {
    companion object {
        operator fun invoke(user: User) =
            with(user) {
                UserResponse(
                    userId = userId!!,
                    username = username,
                    password = password,
                    enabled = enabled,
                    authorities = authorities!!,
                )
            }
    }
}



