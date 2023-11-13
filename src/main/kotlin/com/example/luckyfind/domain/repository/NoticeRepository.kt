package com.example.luckyfind.domain.repository

import com.example.luckyfind.domain.entity.Notice
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository

interface NoticeRepository : JpaRepository<Notice, Long> {

    fun getNoticeById(id : Long) : Notice?
}