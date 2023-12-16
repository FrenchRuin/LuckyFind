package com.example.luckyfind.model

import com.example.luckyfind.domain.entity.Token
import com.example.luckyfind.domain.entity.User

data class TokenRequest(
    var token : String,
)

data class TokenResponse(
    var tokenId: Long,
    var token: String,
){
    companion object {
        operator fun invoke(token : Token) =
            TokenResponse(
                token = token.token,
                tokenId = token.tokenId!!,
            )
    }
}

