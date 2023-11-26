package com.example.luckyfind.controller

import com.example.luckyfind.model.Message
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class MessageController {

    // 메시지 전송 매핑
    @MessageMapping("/chat.sendMessage/{id}")
    @SendTo("/topic/public/{id}")
    fun sendMessage(
        @DestinationVariable id: Long,
        @Payload message: Message?,
        principal: Principal,
    ): Message? {
        message!!.sender = principal.name
        return message
    }

    //채팅참가자 등록
    @MessageMapping("/chat.addUser/{id}")
    @SendTo("/topic/public/{id}")
    fun addUser(
        @DestinationVariable id : Long,
        @Payload message: Message,
        headerAccessor: SimpMessageHeaderAccessor,
        principal: Principal,
    ): Message? {
        headerAccessor.sessionAttributes!!["username"] = principal.name
        message.sender = principal.name
        return message
    }

    //채팅참가자 퇴장
    @MessageMapping("/chat.leaveUser/{id}")
    @SendTo("/topic/public/{id}")
    fun leaveUser(
        @DestinationVariable id : Long,
        @Payload message: Message,
        headerAccessor: SimpMessageHeaderAccessor,
        principal: Principal,
        ): Message? {
        headerAccessor.sessionAttributes!!["username"] = principal.name
        message.sender = principal.name
        return message
    }
}