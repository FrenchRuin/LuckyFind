package com.example.luckyfind.model

import com.example.luckyfind.domain.entity.Recruit
import com.example.luckyfind.domain.enum.RecruitSkill
import com.example.luckyfind.domain.enum.RecruitStatus
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class RecruitRequest(
    val title: String,
    val contents: String,

    @JsonFormat(pattern = "yyyy-MM-dd")
    val recruitDateFrom: LocalDateTime?,

    @JsonFormat(pattern = "yyyy-MM-dd")
    val recruitDateTo: LocalDateTime?,

    val skill: RecruitSkill,
    val status: RecruitStatus,
)


data class RecruitResponse(

    val id: Long,
    val title: String,
    val contents: String,

    val skill: RecruitSkill,
    val status: RecruitStatus,

    @JsonFormat(pattern = "yyyy-MM-dd")
    val recruitDateFrom: LocalDateTime?,

    @JsonFormat(pattern = "yyyy-MM-dd")
    val recruitDateTo: LocalDateTime?,

    @JsonFormat(pattern = "yyyy-MM-dd")
    val createdAt: LocalDateTime?,

    @JsonFormat(pattern = "yyyy-MM-dd")
    val updatedAt: LocalDateTime?,
) {

    companion object {
        operator fun invoke(recruit: Recruit) =
            with(recruit) {
                RecruitResponse(
                    id = id!!,
                    title = title,
                    contents = contents,
                    recruitDateFrom = recruitDateFrom,
                    recruitDateTo = recruitDateTo,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    status = status,
                    skill = skill,
                )
            }
    }
}
