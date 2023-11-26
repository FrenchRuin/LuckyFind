package com.example.luckyfind.service

import com.example.luckyfind.domain.entity.Chat
import com.example.luckyfind.domain.repository.ChatRepository
import com.example.luckyfind.domain.repository.UserRepository
import com.example.luckyfind.exception.UserNotFoundException
import com.example.luckyfind.model.ChatRequest
import com.example.luckyfind.model.ChatResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChatService(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
) {


    // 채팅 생성
    @Transactional
    fun createChat(request: ChatRequest, username : String): ChatResponse {
        val user = userRepository.findByUsername(username) ?: throw UserNotFoundException("유저가 존재하지 않습니다.")
        val chat = Chat(
            title = request.title,
            resnContents = request.resnContents,
            chatType = request.chatType,
            user = user,
        )
        return ChatResponse(chatRepository.save(chat))
    }

    // 채팅 리스트 조회
    @Transactional(readOnly = true)
    fun getChatList() = chatRepository.findAll().map {
        ChatResponse(it)
    }

}