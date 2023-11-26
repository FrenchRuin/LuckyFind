package com.example.luckyfind.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Table
@Entity
class Notice(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var noticeId: Long? = null,

    @Column
    var title: String,

    @Column
    var contents: String,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    val user : User,


    ) : BaseEntity()