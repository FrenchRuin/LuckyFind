package com.example.luckyfind.controller

import com.example.luckyfind.model.RecruitRequest
import com.example.luckyfind.service.RecruitService
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.security.Principal
import javax.print.attribute.standard.Media

@RestController
@RequestMapping("/api/v1/recruit")
class RecruitController(
    private val recruitService: RecruitService,
) {
    // recruit Controller
    @GetMapping("/{id}")
    fun getRecruit(@PathVariable id: Long) =
        recruitService.getRecruit(id)

    @GetMapping("/all")
    fun getRecruitList() =
        recruitService.getRecruitList()

    @PostMapping
    fun addRecruit(
        @RequestBody request: RecruitRequest,
        principal: Principal,
    ) = recruitService.addRecruit(request, principal.name)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteRecruit(@PathVariable id: Long) =
        recruitService.deleteRecruit(id)

}