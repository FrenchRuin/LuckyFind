package com.example.luckyfind.service

import com.example.luckyfind.domain.entity.Token
import com.example.luckyfind.domain.repository.TokenRepository
import com.example.luckyfind.model.TokenResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TokenService(
    private val tokenRepository: TokenRepository,
) {
    @Transactional
    fun addToken(token: Token) =
        tokenRepository.save(token)

    @Transactional(readOnly = true)
    fun getToken(username: String): TokenResponse =
        TokenResponse(tokenRepository.findByUserUsername(username))

    @Transactional
    fun deleteToken(username: String) =
        tokenRepository.deleteByUserUsername(username)
}