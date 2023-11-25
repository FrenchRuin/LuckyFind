package com.example.luckyfind.domain.enum

enum class ChatType {
    ALL, // 전체공유
    TEAM, // 프로젝트 내 공유
    ;

    companion object {
        operator fun invoke(chatType: String) = ChatType.valueOf(chatType.uppercase())
    }
}