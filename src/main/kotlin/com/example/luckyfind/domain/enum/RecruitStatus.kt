package com.example.luckyfind.domain.enum

enum class RecruitStatus {
    // 모집상태
    READY,  // 준비중
    PROGRESS, // 진행중
    FINISH; // 모집완료

    companion object {
        operator fun invoke(recruitStatus: String) = valueOf(recruitStatus.uppercase())
    }

}