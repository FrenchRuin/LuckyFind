package com.example.luckyfind.domain.repository

import com.example.luckyfind.domain.entity.User
import com.example.luckyfind.domain.entity.UserAuthority
import org.springframework.data.jpa.repository.JpaRepository

interface UserAuthorityRepository : JpaRepository<UserAuthority, Long> {
    fun findByUser(user : User) : UserAuthority
}