package com.chamroeurn.iqs.repository

import com.chamroeurn.iqs.repository.entity.QuestionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface QuestionRepository: JpaRepository<QuestionEntity, UUID>