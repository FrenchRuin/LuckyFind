package com.example.luckyfind.controller

import com.example.luckyfind.model.LogInRequest
import com.example.luckyfind.model.LogInResponse
import com.example.luckyfind.model.UserRequest
import com.example.luckyfind.service.UserService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val userService: UserService,
) {

    @PostMapping("/signUp")
    fun signUp(
        @RequestBody request: UserRequest,
    ) = userService.signUp(request)

    @PostMapping("/login")
    fun login(
        request: LogInRequest,
        httpServletResponse: HttpServletResponse
    ) = ResponseEntity.ok(userService.login(request, httpServletResponse))


}