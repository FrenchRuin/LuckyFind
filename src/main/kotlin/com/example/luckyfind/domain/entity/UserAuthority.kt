package com.example.luckyfind.domain.entity

import com.example.luckyfind.domain.enum.AuthorityType
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority

@Entity
@IdClass(UserAuthority::class)
class UserAuthority(
    @Id
    val authorityId: Long,

    @Column
    @Enumerated(EnumType.STRING)
    val authority: AuthorityType,

    ) : GrantedAuthority {

    override fun getAuthority(): String = authority.toString()

}