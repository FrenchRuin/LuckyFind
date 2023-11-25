package com.example.luckyfind.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class PageController {

    // 메인 페이지
    @GetMapping(value = ["/", "/index"])
    fun main() = "index"

    // 공지사항 Notice 페이지
    @GetMapping("/notice")
    fun notice() = "notice/notice"

    // 모집공고 페이지
    @GetMapping("/recruit")
    fun recruit() = "recruit/recruit"

    // 채팅 페이지
    @GetMapping("/chat")
    fun chat() = "chat/chat"
}