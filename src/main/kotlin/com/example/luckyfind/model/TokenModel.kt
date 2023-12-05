package com.example.luckyfind.model

data class TokenModel(
    val grantType : String,
    val accessToken : String,
    val refreshToken : String,
)