package com.example.luckyfind.domain.enum

// 팀원모집간의 스킬요소
enum class RecruitSkill {

    // skill
    JAVA,
    KOTLIN;

    companion object {
        operator fun invoke(skill: String) = valueOf(skill.uppercase())
    }


}