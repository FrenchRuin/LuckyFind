package com.example.luckyfind.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors

@Table(name = "users")
@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userId: Long? = null,

    @Column
    @JvmField
    val username: String,

    @Column
    @JvmField
    val password: String,

    @Column
    val enabled: Boolean,



    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER,cascade = [CascadeType.ALL])
    @JvmField
    var notices: List<Notice>? = null,

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER,cascade = [CascadeType.ALL])
    @JvmField
    var recruits: List<Recruit>? = null,

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER,cascade = [CascadeType.ALL])
    @JvmField
    var chats: List<Chat>? = null,

    ) : UserDetails {

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JvmField
    var authorities: MutableSet<UserAuthority> = mutableSetOf()

    override fun getAuthorities(): MutableSet<UserAuthority> = authorities

    override fun getPassword(): String = password

    override fun getUsername(): String = username

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}
