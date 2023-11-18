package com.example.luckyfind.controller

import com.example.luckyfind.model.RecruitRequest
import com.example.luckyfind.service.RecruitService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/recruit")
class RecruitController(
    private val recruitService: RecruitService,
) {


    @GetMapping("/{id}")
    fun getRecruit(@PathVariable id: Long) =
        recruitService.getRecruit(id)

    @GetMapping
    fun getRecruitList() =
        recruitService.getRecruitList()

    @PostMapping
    fun addRecruit(@RequestBody request: RecruitRequest) =
        recruitService.addRecruit(request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteRecruit(@PathVariable id: Long) =
        recruitService.deleteRecruit(id)

}