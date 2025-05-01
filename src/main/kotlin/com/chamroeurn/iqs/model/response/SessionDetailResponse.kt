package com.chamroeurn.iqs.model.response

import com.chamroeurn.iqs.repository.entity.SessionEntity
import java.time.LocalDateTime
import java.util.*

data class SessionDetailResponse(
    val sessionId: UUID,
    val sessionCode: String,
    val startTime: LocalDateTime?,
    val endTime: LocalDateTime?,
    val quiz: QuizDetailResponse
)


fun SessionEntity.toSessionDetailResponse() = sessionId?.let {
    quiz.toQuizDetailResponse()?.let { quiz ->
        SessionDetailResponse(
        sessionId = it,
        sessionCode = sessionCode,
        startTime = startTime,
        endTime = endTime,
        quiz = quiz
    )
    }
}