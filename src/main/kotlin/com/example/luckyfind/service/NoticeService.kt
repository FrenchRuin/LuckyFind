package com.example.luckyfind.service

import com.example.luckyfind.domain.repository.NoticeRepository
import com.example.luckyfind.exception.NotFoundException
import com.example.luckyfind.model.NoticeResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class NoticeService (
    private val noticeRepository: NoticeRepository,
) {

    // Notice정보를 가져온다
    @Transactional(readOnly = true)
    fun getNotice(id : Long) : NoticeResponse{
        val notice = noticeRepository.findByIdOrNull(id) ?: throw NotFoundException("해당 공지사항이 존재하지 않습니다.")
        return NoticeResponse(notice)
    }
}