package com.example.luckyfind.domain.repository

import com.example.luckyfind.domain.entity.Recruit
import org.springframework.data.jpa.repository.JpaRepository

interface RecruitRepository : JpaRepository<Recruit, Long> {

}