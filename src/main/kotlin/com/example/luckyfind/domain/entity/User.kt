package com.example.luckyfind.domain.entity

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

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(foreignKey = ForeignKey(name = "userId"))
    @JvmField
    var authorities: MutableSet<UserAuthority>? = null,

    ) : UserDetails {

    override fun getAuthorities(): MutableSet<UserAuthority>? = authorities

    override fun getPassword(): String = password

    override fun getUsername(): String = username

    override fun isAccountNonExpired(): Boolean = enabled

    override fun isAccountNonLocked(): Boolean = enabled

    override fun isCredentialsNonExpired(): Boolean = enabled

    override fun isEnabled(): Boolean = enabled
}
