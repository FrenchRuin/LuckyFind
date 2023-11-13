package com.example.luckyfind.controller

import com.example.luckyfind.model.NoticeRequest
import com.example.luckyfind.service.NoticeService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/notice")
class NoticeController(
    private val noticeService: NoticeService,
) {

    // 공지사항 정보를 가져오기 위한 Notice API
    // 공지사항 해당 ID로 가져오기
    @GetMapping("/{id}")
    fun getNotice(@PathVariable id: Long) =
        noticeService.getNotice(id)

    // 공지사항 생성
    @PostMapping
    fun createNotice(
        @RequestBody request : NoticeRequest,
    ) = noticeService.createNotice(request)
}