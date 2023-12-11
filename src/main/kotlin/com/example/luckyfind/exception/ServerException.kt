package com.example.luckyfind.exception

sealed class ServerException(
    val code: Int,
    override val message: String?,
) : RuntimeException(message)

// NotFoundException 커스텀 처리
data class NotFoundException(
    override val message: String,
) : ServerException(404, message)

// UserNotFoundException 커스텀 처리
data class UserNotFoundException(
    override val message: String,
) : ServerException(404, message)

// 토큰 권한 없을시 예외 커스텀 처리
data class TokenException(
    override val message: String,
) : ServerException(404, message)

// Cookie 없을시 Exception
data class CookieNotFoundException(
    override val message: String,
) : ServerException(404, message)
