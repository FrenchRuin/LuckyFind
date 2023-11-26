package com.example.luckyfind.controller

import com.example.luckyfind.model.ChatRequest
import com.example.luckyfind.service.ChatService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "Chat API", description = "Chat 서비스 API Test")
@RestController
@RequestMapping("/api/v1")
class ChatController(
    private val chatService: ChatService,
) {

    // 채팅생성
    @PostMapping("/chat")
    fun createChat(
        @RequestBody request: ChatRequest,
    ) = chatService.createChat(request)

    // 모든 채팅방 호출
    @GetMapping("/chat/all")
    @Operation(summary = "모든 채팅방을 호출합니다.")
    fun getChatList() = chatService.getChatList()
}