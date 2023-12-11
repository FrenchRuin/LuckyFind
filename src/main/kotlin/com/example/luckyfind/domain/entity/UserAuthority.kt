package com.example.luckyfind.domain.entity

import com.example.luckyfind.domain.enum.AuthorityType
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority

@Entity
//@IdClass(UserAuthority::class)
data class UserAuthority(
    @Id
    val authorityId: Long,

//    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    val user : User,

    @Column
    @Enumerated(EnumType.STRING)
    val authority: AuthorityType,

    ) : GrantedAuthority {

    override fun getAuthority(): String = authority.toString()

}