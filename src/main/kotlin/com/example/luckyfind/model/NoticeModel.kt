package com.example.luckyfind.model

import com.example.luckyfind.domain.entity.Notice
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


// Notice Entity Request
data class NoticeRequest(
    val title: String,
    val contents: String,
)

// Notice Entity Response
data class NoticeResponse(
    val id: Long,
    val title: String,
    val contents: String,

    @JsonFormat(pattern = "yyyy-MM-dd")
    val createdAt: LocalDateTime?,

    @JsonFormat(pattern = "yyyy-MM-dd")
    val updatedAt: LocalDateTime?,
) {
    companion object {
        operator fun invoke(notice: Notice) =
            with(notice) {
                NoticeResponse(
                    id = id!!,
                    title = title,
                    contents = contents,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                )
            }

    }
}

