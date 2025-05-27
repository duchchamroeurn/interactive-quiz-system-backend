package com.chamroeurn.iqs.model.response

import com.chamroeurn.iqs.repository.entity.QuizEntity
import java.time.LocalDateTime
import java.util.*

data class QuizDetailResponse(
    val id: UUID,
    val title: String,
    val description: String?,
    val createdAt: LocalDateTime,
    val questions: List<QuestionWithOptionResponse>
)

fun QuizEntity.toQuizDetailResponse() = quizId?.let { id ->
    QuizDetailResponse(
        id = id,
        title = title,
        description = description,
        createdAt = createdAt,
        questions = questions.mapNotNull { it.toQuestionWithOptionResponse() }
    )
}