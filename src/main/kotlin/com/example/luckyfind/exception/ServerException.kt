package com.example.luckyfind.exception

sealed class ServerException(
    val code : Int,
    override val message: String?,
) : RuntimeException(message)

// NotFoundException 커스텀 처리
data class NotFoundException(
    override val message: String,
) : ServerException(404, message)