package com.example.luckyfind.domain.entity

import com.example.luckyfind.domain.enum.ChatType
import jakarta.persistence.*

@Table
@Entity
class Chat(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

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

) : BaseEntity()