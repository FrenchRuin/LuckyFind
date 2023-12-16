package com.example.luckyfind.domain.entity

import jakarta.persistence.*

@Entity
@Table
class Token (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var tokenId: Long? = null,

    @OneToOne
    @JoinColumn(name = "user_id")
    var user : User,

    @Column
    var token : String,
)