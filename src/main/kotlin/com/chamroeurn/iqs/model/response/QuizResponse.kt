package com.chamroeurn.iqs.model.response

import com.chamroeurn.iqs.repository.entity.QuizEntity
import java.time.LocalDateTime
import java.util.UUID

data class QuizResponse(
    val id: UUID,
    val title: String,
    val description: String?,
    val createdAt: LocalDateTime
)

fun QuizEntity.toResponse() =
    quizId?.let { QuizResponse(id = it, title = title, description = description, createdAt = createdAt) }