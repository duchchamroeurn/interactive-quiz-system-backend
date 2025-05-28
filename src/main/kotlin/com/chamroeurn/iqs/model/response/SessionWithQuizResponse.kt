package com.chamroeurn.iqs.model.response

import com.chamroeurn.iqs.repository.entity.SessionEntity
import java.util.UUID

data class SessionWithQuizResponse(
    val sessionId: UUID,
    val quiz: QuizResponse,
)

fun SessionEntity.toSessionWithQuizResponse() = sessionId?.let {
    quiz.toResponse()?.let { quiz ->
        SessionWithQuizResponse(
        sessionId = it,
        quiz = quiz
    )
    }
}