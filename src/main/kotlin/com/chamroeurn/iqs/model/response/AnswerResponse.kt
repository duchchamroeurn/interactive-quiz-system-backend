package com.chamroeurn.iqs.model.response

import com.chamroeurn.iqs.repository.entity.AnswerEntity
import java.time.LocalDateTime
import java.util.UUID

data class AnswerResponse(
    val answerId: UUID,
    val sessionCode: String,
    val username: String,
    val question: String,
    val answerSubmit: String,
    val answerTime: LocalDateTime?
)

fun AnswerEntity.toAnswerResponse() = answerId?.let {
    AnswerResponse(
        answerId = it,
        sessionCode = session.sessionCode,
        username = user.username,
        question = question.questionText,
        answerSubmit = option.optionText,
        answerTime = answerTime
    )
}