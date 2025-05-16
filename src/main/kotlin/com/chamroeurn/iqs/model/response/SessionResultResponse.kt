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
        val totalPoint = answers.count().toDouble() // Ensure Double for calculations
        val totalEarnedPoint =
            answers.filter { it.question.type == QuestionTypes.MULTIPLE_CHOICE }.count { it.option!!.isCorrect }
                .plus(answers.filter { it.question.type == QuestionTypes.TRUE_FALSE }
                    .count { it.replyAnswer == it.question.correctAnswer }).toDouble() // Handle null option
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