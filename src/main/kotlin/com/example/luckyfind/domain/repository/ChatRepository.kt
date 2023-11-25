package com.example.luckyfind.domain.repository

import com.example.luckyfind.domain.entity.Chat
import org.springframework.data.jpa.repository.JpaRepository

interface ChatRepository : JpaRepository<Chat, Long> {
}