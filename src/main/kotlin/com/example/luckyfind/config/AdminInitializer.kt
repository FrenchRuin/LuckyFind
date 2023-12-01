package com.example.luckyfind.config

import com.example.luckyfind.domain.entity.User
import com.example.luckyfind.domain.repository.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class AdminInitializer(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        userRepository.save(
            User(
                username = "admin",
                password = passwordEncoder.encode("1234"),
                enabled = true,
            )
        )
    }
}