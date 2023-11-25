package com.example.luckyfind.controller

import com.example.luckyfind.model.ChatMessage
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.web.bind.annotation.RestController

@RestController
class MessageController {

    // 메시지 전송 매핑
    @MessageMapping("/chat.sendMessage/{id}")
    @SendTo("/topic/public/{id}")
    fun sendMessage(
        @DestinationVariable id: Long,
        @Payload chatMessage: ChatMessage?
    ): ChatMessage? {
        return chatMessage
    }

    //채팅참가자 등록
    @MessageMapping("/chat.addUser/{id}")
    @SendTo("/topic/public/{id}")
    fun addUser(
        @DestinationVariable id : Long,
        @Payload chatMessage: ChatMessage,
        headerAccessor: SimpMessageHeaderAccessor,
    ): ChatMessage? {
        headerAccessor.sessionAttributes!!["username"] = chatMessage.sender
        return chatMessage
    }

    //채팅참가자 퇴장
    @MessageMapping("/chat.leaveUser/{id}")
    @SendTo("/topic/public/{id}")
    fun leaveUser(
        @DestinationVariable id : Long,
        @Payload chatMessage: ChatMessage,
        headerAccessor: SimpMessageHeaderAccessor): ChatMessage? {
        headerAccessor.sessionAttributes!!["username"] = chatMessage.sender
        return chatMessage
    }
}