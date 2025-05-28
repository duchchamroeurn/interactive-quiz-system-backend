package com.chamroeurn.iqs.model.response

import com.chamroeurn.iqs.repository.entity.QuestionEntity
import java.util.UUID

data class QuestionResponse(
    val id: UUID,
    val questionText: String,
    val time: Int,
    val type: String,
    val isCustomize: Boolean,
    val correctAnswer: Boolean?
)

fun QuestionEntity.toResponse() =
    questionId?.let {
        QuestionResponse(
            id = it,
            questionText = questionText,
            time = timeLimitInSecond,
            type = type.name,
            isCustomize = isCustomize == true,
            correctAnswer = correctAnswer
        )
    }