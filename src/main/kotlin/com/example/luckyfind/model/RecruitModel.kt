package com.example.luckyfind.model

import com.example.luckyfind.domain.entity.Recruit
import com.example.luckyfind.domain.enum.RecruitSkill
import com.example.luckyfind.domain.enum.RecruitStatus
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.time.LocalDateTime

data class RecruitRequest(
    val title: String,
    val contents: String,

    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @JsonFormat(pattern = "yyyy-MM-dd")
    val recruitDateFrom: LocalDate?,

    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @JsonFormat(pattern = "yyyy-MM-dd")
    val recruitDateTo: LocalDate?,

    val skill: RecruitSkill,
    val status: RecruitStatus,
)


data class RecruitResponse(

    val recruitId: Long,
    val title: String,
    val contents: String,

    val skill: RecruitSkill,
    val status: RecruitStatus,

    val imageUrl: String?,

    @JsonFormat(pattern = "yyyy-MM-dd")
    val recruitDateFrom: LocalDate?,

    @JsonFormat(pattern = "yyyy-MM-dd")
    val recruitDateTo: LocalDate?,

    @JsonFormat(pattern = "yyyy-MM-dd")
    val createdAt: LocalDateTime?,

    @JsonFormat(pattern = "yyyy-MM-dd")
    val updatedAt: LocalDateTime?,
) {

    companion object {
        operator fun invoke(recruit: Recruit) =
            with(recruit) {
                RecruitResponse(
                    recruitId = recruitId!!,
                    title = title,
                    contents = contents,
                    recruitDateFrom = recruitDateFrom,
                    recruitDateTo = recruitDateTo,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    status = status,
                    skill = skill,
                    imageUrl = if (imageUrl.isNullOrEmpty()) null else "http://localhost:9090/images/$imageUrl",
                )
            }
    }
}
