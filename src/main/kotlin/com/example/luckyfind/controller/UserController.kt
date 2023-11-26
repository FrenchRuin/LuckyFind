package com.example.luckyfind.controller

import com.example.luckyfind.domain.repository.UserRepository
import com.example.luckyfind.model.UserRequest
import com.example.luckyfind.model.UserResponse
import com.example.luckyfind.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/v1/user")
class UserController (
    private val userService: UserService,
){

    @PostMapping("/signUp")
    fun signUp(
        @RequestBody request : UserRequest,
    ) = userService.signUp(request)


    @GetMapping
    fun getUserInfo(
        principal: Principal,
    ) : UserResponse
      = userService.findUser(principal.name)


}