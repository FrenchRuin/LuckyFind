package com.example.luckyfind.domain.enum

enum class MessageType {
    CHAT, // 채팅
    JOIN, // 참가
    LEAVE // 퇴장
    ;

    companion object {
        operator fun invoke(messageType : String) = MessageType.valueOf(messageType.uppercase())
    }
}