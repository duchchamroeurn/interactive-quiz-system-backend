package com.chamroeurn.iqs.model.response

import com.chamroeurn.iqs.repository.entity.AnswerEntity
import com.chamroeurn.iqs.repository.entity.QuestionTypes
import java.time.LocalDateTime
import java.util.*

data class SessionResultResponse(
    val sessionId: UUID,
    val sessionCode: String,
    val startTime: LocalDateTime?,
    val endTime: LocalDateTime?,
    val participants: List<ParticipantSessionResponse> // Use immutable List
)

data class ParticipantSessionResponse(
    val userId: String,
    val email: String,
    val username: String,
    val examResult: Boolean,
    val totalPoint: Double,
    val totalEarnedPoint: Double,
    val percentage: Double // Added percentage
)

fun MutableList<AnswerEntity>.toSessionResultResponse(): SessionResultResponse? {

    val session = first().session
    val participants: List<ParticipantSessionResponse> = groupBy { it.user }.map { (user, answers) ->
        val totalPoint = session.quiz.questions.count().toDouble() // Ensure Double for calculations
        val totalEarnedPoint = answers.sumOf { answer ->
            when (answer.question.type) {
                QuestionTypes.MULTIPLE_CHOICE -> {
                    // Ensure option is not null and is correct
                    if (answer.option?.isCorrect == true) 1.0 else 0.0
                }
                QuestionTypes.TRUE_FALSE, QuestionTypes.YES_NO -> {
                    if (answer.question.isCustomize == true) {
                        if (answer.option?.isCorrect == true) 1.0 else 0.0
                    } else {
                        if (answer.replyAnswer!!.and(answer.question.correctAnswer!!)) 1.0 else 0.0
                    }

                }
            }
        }
        val percentage = if (totalPoint > 0) (totalEarnedPoint / totalPoint) * 100 else 0.0  // Avoid division by zero
        val examResult = percentage >= 50.0 // Use Double for comparison

        ParticipantSessionResponse(
            userId = user.userId.toString(), // Assuming userId is not already a String
            email = user.email,
            username = user.username,
            examResult = examResult,
            totalPoint = totalPoint,
            totalEarnedPoint = totalEarnedPoint,
            percentage = percentage
        )
    }

    return session.sessionId?.let {
        SessionResultResponse(
            sessionId = it,
            sessionCode = session.sessionCode,
            startTime = session.startTime,
            endTime = session.endTime,
            participants = participants
        )
    }

}