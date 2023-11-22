package com.example.luckyfind.model

import com.example.luckyfind.domain.enum.MessageType

data class ChatMessage(
    var type: MessageType,
    var content: String?,
    var sender: String?
)


