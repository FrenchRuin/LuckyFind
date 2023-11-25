package com.example.luckyfind.controller

import com.example.luckyfind.model.ChatMessage
import com.example.luckyfind.model.ChatRequest
import com.example.luckyfind.service.ChatService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Chat API", description = "Chat 서비스 API Test")
@RestController
class ChatController(
    private val chatService: ChatService,
) {

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


    // 채팅생성
    @PostMapping("/api/v1/chat")
    fun createChat(
        @RequestBody request: ChatRequest,
    ) = chatService.createChat(request)

    @GetMapping("/api/v1/chat/all")
    @Operation(summary = "모든 채팅방을 호출합니다.")
    fun getChatList() = chatService.getChatList()
}