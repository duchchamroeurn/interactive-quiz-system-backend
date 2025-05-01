package com.chamroeurn.iqs.service

import com.chamroeurn.iqs.exception.RestErrorResponseException
import com.chamroeurn.iqs.model.response.*
import com.chamroeurn.iqs.repository.QuizRepository
import com.chamroeurn.iqs.repository.SessionRepository
import com.chamroeurn.iqs.repository.entity.SessionEntity
import com.chamroeurn.iqs.utils.SessionCodeGenerator
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class SessionService(
    private val sessionRepository: SessionRepository,
    private val quizRepository: QuizRepository,
    private val sessionCodeGenerator: SessionCodeGenerator
) {

    fun createNewSession(quizId: UUID): SuccessResponse<SessionResponse> {

        val quiz = quizRepository.findById(quizId).orElseThrow {
            val problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                "The content you are trying to access does not exist."
            )
            throw RestErrorResponseException(problemDetail)
        }

        val sessionCode = generateTrulyUniqueSessionCode()

        val newSession = SessionEntity(
            quiz = quiz,
            sessionCode = sessionCode,
            startTime = LocalDateTime.now()
        )

        val savedSession = sessionRepository.save(newSession)

        return SuccessResponse(
            message = "Great! You have successfully created the session.",
            data = savedSession.toResponse()

        )

    }

    fun fetchBySessionCode(sessionCode: String): SuccessResponse<SessionDetailResponse> {
        val session = sessionRepository.findBySessionCode(sessionCode)

        if (session == null) {
            val problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                "The content you are trying to access does not exist."
            )
            throw RestErrorResponseException(problemDetail)
        }

        if (session.endTime != null) {
            val problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN,
                "This session has ended and is no longer accessible for participation."
            )
            throw RestErrorResponseException(problemDetail)
        }

        return SuccessResponse(
            data = session.toSessionDetailResponse(),
            message = "We found the session you were looking for."
        )

    }

    fun endSession(sessionId: UUID): SuccessResponse<SessionResponse> {

        val session = sessionRepository.findById(sessionId).orElseThrow {
            val problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                "The content you are trying to access does not exist."
            )
            throw RestErrorResponseException(problemDetail)
        }

        if (session.endTime != null) {
            val problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN,
                "This session has ended and is no longer accessible for participation."
            )
            throw RestErrorResponseException(problemDetail)
        }

        session.endTime = LocalDateTime.now()

        val updatedSession = sessionRepository.save(session)

        return SuccessResponse(
            message = "Great! You have successfully end the session.",
            data = updatedSession.toResponse()

        )
    }

    private fun generateTrulyUniqueSessionCode(): String {
        var sessionCode: String
        do {
            sessionCode = sessionCodeGenerator.generateUniqueSessionCode()
        } while (sessionRepository.existsBySessionCode(sessionCode)) // Assuming you have this method in your repository
        return sessionCode
    }
}