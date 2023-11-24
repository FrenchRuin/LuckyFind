package com.example.luckyfind.controller

import com.example.luckyfind.model.ChatMessage
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatController {

    // 메시지 전송 매핑
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    fun sendMessage(
        @Payload chatMessage: ChatMessage?
    ): ChatMessage? {
        return chatMessage
    }

    //채팅참가자 등록
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    fun addUser(@Payload chatMessage: ChatMessage, headerAccessor: SimpMessageHeaderAccessor): ChatMessage? {
        headerAccessor.sessionAttributes!!["username"] = chatMessage.sender
        return chatMessage
    }

    //채팅참가자 퇴장
    @MessageMapping("/chat.leaveUser")
    @SendTo("/topic/public")
    fun leaveUser(@Payload chatMessage: ChatMessage, headerAccessor: SimpMessageHeaderAccessor): ChatMessage? {
        headerAccessor.sessionAttributes!!["username"] = chatMessage.sender
        return chatMessage
    }
}