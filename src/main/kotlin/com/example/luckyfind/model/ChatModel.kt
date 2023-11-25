package com.example.luckyfind.model

import com.example.luckyfind.domain.entity.Chat
import com.example.luckyfind.domain.enum.ChatType
import com.example.luckyfind.domain.enum.MessageType
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class ChatMessage(
    var type: MessageType,
    var content: String?,
    var sender: String?
)

data class ChatRequest(
    val title: String,
    val resnContents: String,
    val chatType: ChatType,
)

data class ChatResponse(
    val id: Long,
    val title: String,
    val resnContents: String,
    val chatType: ChatType,
    @JsonFormat(pattern = "yyyy-MM-dd")
    val createdAt: LocalDateTime?,

    @JsonFormat(pattern = "yyyy-MM-dd")
    val updatedAt: LocalDateTime?,

    ) {
    companion object {
        operator fun invoke(chat: Chat) =
            with(chat) {
                ChatResponse(
                    id = id!!,
                    title = title,
                    resnContents = resnContents,
                    chatType = chatType,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                )
            }
    }
}


