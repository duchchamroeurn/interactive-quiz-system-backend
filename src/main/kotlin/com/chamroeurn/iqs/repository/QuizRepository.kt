package com.chamroeurn.iqs.repository

import com.chamroeurn.iqs.repository.entity.QuizEntity
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface QuizRepository: JpaRepository<QuizEntity, UUID> {
    @Query("SELECT q FROM QuizEntity q WHERE LOWER(q.title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    fun findByTitleIgnoreCaseContainingWithLimit(@Param("searchTerm") searchTerm: String, pageable: PageRequest): List<QuizEntity>

}