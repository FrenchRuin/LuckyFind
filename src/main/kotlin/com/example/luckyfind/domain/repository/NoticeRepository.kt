package com.example.luckyfind.domain.repository

import com.example.luckyfind.domain.entity.Notice
import com.example.luckyfind.model.NoticeResponse
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface NoticeRepository : JpaRepository<Notice, Long> {

   fun getNoticeByNoticeId(noticeId : Long) : Notice?

}