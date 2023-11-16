package com.example.luckyfind.controller

import com.example.luckyfind.model.NoticeRequest
import com.example.luckyfind.service.NoticeService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


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
        @RequestBody request: NoticeRequest,
    ) = noticeService.createNotice(request)

    // 공지사항 모두 조회
    @GetMapping("/all")
    fun getNoticeList() = noticeService.getNoticeList()

    // 공지사항 수정
    @PutMapping("/{id}")
    fun editNotice(
        @PathVariable id: Long,
        request: NoticeRequest
    ) {
        noticeService.editNotice(request, id)
    }

    //  공지사항 삭제
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteNotice(@PathVariable id: Long) = noticeService.deleteNotice(id)
}