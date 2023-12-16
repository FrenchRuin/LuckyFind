package com.example.luckyfind.domain.repository

import com.example.luckyfind.domain.entity.Token
import org.springframework.data.jpa.repository.JpaRepository

interface TokenRepository : JpaRepository<Token, Long> {

    fun findByUserUsername(username : String) : Token

    fun deleteByUserUsername(username: String)
}