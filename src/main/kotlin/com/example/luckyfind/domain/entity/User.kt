package com.example.luckyfind.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.springframework.security.core.userdetails.UserDetails

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
    var authorities: MutableSet<UserAuthority>? = null,

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

    override fun getAuthorities(): MutableSet<UserAuthority>? = authorities

    override fun getPassword(): String = password

    override fun getUsername(): String = username

    override fun isAccountNonExpired(): Boolean = enabled

    override fun isAccountNonLocked(): Boolean = enabled

    override fun isCredentialsNonExpired(): Boolean = enabled

    override fun isEnabled(): Boolean = enabled
}
