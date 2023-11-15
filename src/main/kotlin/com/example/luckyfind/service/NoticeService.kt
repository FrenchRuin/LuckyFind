package com.example.luckyfind.service

import com.example.luckyfind.domain.entity.Notice
import com.example.luckyfind.domain.repository.NoticeRepository
import com.example.luckyfind.exception.NotFoundException
import com.example.luckyfind.model.NoticeRequest
import com.example.luckyfind.model.NoticeResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class NoticeService(
    private val noticeRepository: NoticeRepository,
) {

//    with(notice) {
//        NoticeResponse(
//            id = id!!,
//            title = title,
//            contents = contents,
//            createdAt = createdAt,
//            updatedAt = updatedAt,
//        )
//    }

    // Notice정보를 가져온다
    @Transactional(readOnly = true)
    fun getNotice(id: Long): NoticeResponse {
        val notice = noticeRepository.findByIdOrNull(id) ?: throw NotFoundException("해당 공지사항이 존재하지 않습니다.")
        return NoticeResponse(notice)
    }


    // Notice를 생성한다.
    @Transactional
    fun createNotice(request: NoticeRequest): NoticeResponse {
        val notice = Notice(
            title = request.title,
            contents = request.contents
        )
        return NoticeResponse(noticeRepository.save(notice))
    }

    // Notice 생성일자를 기준으로 모든 데이터 조회
    @Transactional(readOnly = true)
    fun getNoticeList(): MutableList<Notice> = noticeRepository.findAll()


    // Notice 삭제
    fun deleteNotice(id: Long) {
        noticeRepository.deleteById(id)
    }


    // notice 수정
    @Transactional
    fun editNotice(request: NoticeRequest, id: Long) : NoticeResponse {
        val notice: Notice = noticeRepository.findByIdOrNull(id) ?: throw NotFoundException("공지사항이 존재하지 않습니다.")

        return with(notice) {
            title = request.title
            contents = request.contents
            NoticeResponse(noticeRepository.save(this))
        }
    }
}