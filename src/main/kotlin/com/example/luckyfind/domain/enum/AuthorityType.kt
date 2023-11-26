package com.example.luckyfind.domain.enum

enum class AuthorityType {

    ROLE_USER,
    ROLE_ADMIN;

    companion object {
        operator fun invoke(authorityType: String) = AuthorityType.valueOf(authorityType.uppercase())
    }
}