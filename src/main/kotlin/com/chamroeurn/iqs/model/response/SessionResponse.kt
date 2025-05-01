package com.chamroeurn.iqs.model.response

import com.chamroeurn.iqs.repository.entity.SessionEntity
import java.time.LocalDateTime
import java.util.UUID

data class SessionResponse(
    val sessionId: UUID,
    val sessionCode: String,
    val startTime: LocalDateTime?,
    val endTime: LocalDateTime?
)

fun SessionEntity.toResponse() = sessionId?.let {
    SessionResponse(
        sessionId = it,
        sessionCode = sessionCode,
        startTime = startTime,
        endTime = endTime
    )
}
