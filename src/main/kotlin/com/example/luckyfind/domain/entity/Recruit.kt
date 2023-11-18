package com.example.luckyfind.domain.entity

import com.example.luckyfind.domain.enum.RecruitSkill
import com.example.luckyfind.domain.enum.RecruitStatus
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Table
@Entity
class Recruit (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long? = null,

    @Column
    var title : String,

    @Column
    var contents : String,

    @Column
    @Enumerated(EnumType.STRING)
    var skill : RecruitSkill,

    @Column
    @Enumerated(EnumType.STRING)
    var status : RecruitStatus,

    @Column
    var recruitDateFrom : LocalDateTime? = null,

    @Column
    var recruitDateTo : LocalDateTime? = null,

):BaseEntity()