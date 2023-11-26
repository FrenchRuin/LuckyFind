package com.example.luckyfind.domain.entity

import com.example.luckyfind.domain.enum.ChatType
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Table
@Entity
class Chat(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var chatId: Long? = null,

    // 제목
    @Column
    var title: String,

    // 사유
    @Column
    var resnContents: String,

    // 채팅 종류
    @Column
    @Enumerated(EnumType.STRING)
    var chatType: ChatType,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    val user : User,

) : BaseEntity()