package com.chamroeurn.iqs.model.response

import com.chamroeurn.iqs.repository.entity.QuestionEntity
import java.util.UUID

data class QuestionResponse(
    val id: UUID,
    val questionText: String,
    val time: Int
)

fun QuestionEntity.toResponse() =
    questionId?.let { QuestionResponse(id = it, questionText = questionText, time = timeLimitInSecond) }