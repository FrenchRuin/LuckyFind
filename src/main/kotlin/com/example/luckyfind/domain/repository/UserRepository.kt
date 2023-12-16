package com.example.luckyfind.domain.repository

import com.example.luckyfind.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UserRepository : JpaRepository<User, Long> {
    @Query("select u from User u where u.username = :username")
    fun findByUsername(@Param("username") username: String): User?
}