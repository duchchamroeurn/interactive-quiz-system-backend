package com.chamroeurn.iqs.repository

import com.chamroeurn.iqs.repository.entity.QuizEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface QuizRepository: JpaRepository<QuizEntity, UUID>