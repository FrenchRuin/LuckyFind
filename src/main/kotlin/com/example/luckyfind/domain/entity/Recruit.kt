package com.example.luckyfind.domain.entity

import com.example.luckyfind.domain.enum.RecruitSkill
import com.example.luckyfind.domain.enum.RecruitStatus
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Table
@Entity
class Recruit(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val recruitId: Long? = null,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    val user : User,

    @Column
    var title: String,

    @Column
    var contents: String,

    @Column
    var imageUrl: String? = null,

    @Column
    @Enumerated(EnumType.STRING)
    var skill: RecruitSkill,

    @Column
    @Enumerated(EnumType.STRING)
    var status: RecruitStatus,

    @Column
    var recruitDateFrom: LocalDate? = null,

    @Column
    var recruitDateTo: LocalDate? = null,

    ) : BaseEntity()