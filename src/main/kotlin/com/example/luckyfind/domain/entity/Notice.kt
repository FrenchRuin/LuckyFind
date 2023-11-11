package com.example.luckyfind.domain.entity

import jakarta.persistence.*

@Table(name = "notice")
@Entity
class Notice (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long? = null,

    @Column
    var title : String,

    @Column
    var contents : String,

): BaseEntity()