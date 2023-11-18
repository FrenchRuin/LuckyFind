package com.example.luckyfind.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class PageController {

    // 메인 페이지
    @GetMapping(value = ["/","/index"])
    fun main() = "index"

    // 공지사항 Notice 페이지
    @GetMapping("/notice")
    fun notice() = "notice/notice"

    @GetMapping("/recruit")
    fun recruit() = "recruit/recruit"

}