package com.example.luckyfind.service

import com.example.luckyfind.domain.entity.Recruit
import com.example.luckyfind.domain.repository.RecruitRepository
import com.example.luckyfind.domain.repository.UserRepository
import com.example.luckyfind.exception.NotFoundException
import com.example.luckyfind.exception.UserNotFoundException
import com.example.luckyfind.model.RecruitRequest
import com.example.luckyfind.model.RecruitResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping

@Service
class RecruitService(
    private val recruitRepository: RecruitRepository,
    private val userRepository: UserRepository,
) {

    // 모집공고 조회
    @Transactional(readOnly = true)
    fun getRecruit(id: Long): RecruitResponse {
        val recruit = recruitRepository.findByIdOrNull(id) ?: throw NotFoundException("해당 모집공고가 존재하지 않습니다.")
        return RecruitResponse(recruit)
    }

    // 모집 공고 추가
    @Transactional
    fun addRecruit(request: RecruitRequest, username : String): RecruitResponse {
        val user = userRepository.findByUsername(username) ?: throw UserNotFoundException("유저가 존재하지 않습니다.")
        val recruit = Recruit(
            title = request.title,
            contents = request.title,
            skill = request.skill,
            status = request.status,
            recruitDateFrom = request.recruitDateFrom,
            recruitDateTo = request.recruitDateTo,
            user = user,
        )
        return RecruitResponse(recruitRepository.save(recruit))
    }

    @Transactional(readOnly = true)
    fun getRecruitList() = recruitRepository.findAll().map {
        RecruitResponse(it)
    }

    //모집공고 삭제
    fun deleteRecruit(id: Long) =
        recruitRepository.deleteById(id)

}