package com.chamroeurn.iqs.service

import com.chamroeurn.iqs.exception.RestErrorResponseException
import com.chamroeurn.iqs.model.request.StartSessionRequest
import com.chamroeurn.iqs.model.response.*
import com.chamroeurn.iqs.repository.QuizRepository
import com.chamroeurn.iqs.repository.SessionRepository
import com.chamroeurn.iqs.repository.UserRepository
import com.chamroeurn.iqs.repository.entity.QuizEntity
import com.chamroeurn.iqs.repository.entity.SessionAccessType
import com.chamroeurn.iqs.repository.entity.SessionEntity
import com.chamroeurn.iqs.repository.entity.UserEntity
import com.chamroeurn.iqs.utils.SessionCodeGenerator
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service
class SessionService(
    private val sessionRepository: SessionRepository,
    private val quizRepository: QuizRepository,
    private val userRepository: UserRepository,
    private val sessionCodeGenerator: SessionCodeGenerator
) {
    @Transactional
    fun createNewSession(requestBody: StartSessionRequest): SuccessResponse<SessionResponse> {
        try {
            val quizId = UUID.fromString(requestBody.quizId)
            val quiz = quizRepository.findById(quizId).orElseThrow {
                val problemDetail = ProblemDetail.forStatusAndDetail(
                    HttpStatus.NOT_FOUND,
                    "The content you are trying to access does not exist."
                )
                throw RestErrorResponseException(problemDetail)
            }

            val isPrivateAccess = requestBody.users.isNotEmpty()
            val savedSession = if (isPrivateAccess) {
                createPrivateSession(quiz, requestBody.users.mapNotNull { UUID.fromString(it) })
            } else {
                createPublicSession(quiz)
            }

            return SuccessResponse(
                message = "Great! You have successfully created the session.",
                data = savedSession.toResponse()

            )
        } catch (error: Exception) {
            val problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                "The content you are trying to access does not exist."
            )
            throw RestErrorResponseException(problemDetail)
        }
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

    fun fetchBySessionId(sessionId: UUID): SuccessResponse<SessionDetailResponse> {
        val session = sessionRepository.findById(sessionId).orElseThrow {
            val problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                "The content you are trying to access does not exist."
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

        if (isSessionEnded(session)) {
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

    @Transactional
    fun deleteSession(sessionId: UUID): SuccessResponse<String> {
        try {
            sessionRepository.deleteById(sessionId)
            return SuccessResponse(
                message = "Great! You have successfully deleted the session.",
                data = "Session was remove."
            )
        } catch (error: Exception) {
            val problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                error.message
            )
            throw RestErrorResponseException(problemDetail)
        }
    }

    fun allSessions(pageRequest: PageRequest): PagedResponse<SessionResponse> {

        val pageSession = sessionRepository.findAll(pageRequest)

        return PagedResponse(
            totalElements = pageSession.totalElements,
            size = pageSession.size,
            totalPages = pageSession.totalPages,
            page = pageRequest.pageNumber,
            data = pageSession.content.mapNotNull { it.toResponse() }
        )
    }

    private fun isSessionEnded(session: SessionEntity): Boolean {
        val endTime = session.endTime
        return endTime != null && LocalDateTime.now().isAfter(endTime)
    }

    private fun generateTrulyUniqueSessionCode(): String {
        var sessionCode: String
        do {
            sessionCode = sessionCodeGenerator.generateUniqueSessionCode()
        } while (sessionRepository.existsBySessionCode(sessionCode)) // Assuming you have this method in your repository
        return sessionCode
    }

    fun getSessionById(sessionId: UUID, currentUser: UserEntity?): SessionEntity? {
        val session = sessionRepository.findById(sessionId).orElse(null) ?: return null

        return when (session.accessType) {
            SessionAccessType.PUBLIC -> {
                // Anyone can access public sessions
                session
            }

            SessionAccessType.PRIVATE -> {
                // Only specific users can access private sessions
                if (currentUser != null && session.accessibleUsers.contains(currentUser)) {
                    session
                } else {
                    null // Or throw an UnauthorizedException
                }
            }
        }
    }

    private fun createPublicSession(quiz: QuizEntity): SessionEntity {
        val sessionCode = generateTrulyUniqueSessionCode()

        return sessionRepository.save(
            SessionEntity(
                quiz = quiz,
                sessionCode = sessionCode,
                accessType = SessionAccessType.PUBLIC,
                startTime = LocalDateTime.now()

            )
        )
    }

    @Transactional
    private fun createPrivateSession(quiz: QuizEntity, allowedUserIds: List<UUID>): SessionEntity {
        val sessionCode = generateTrulyUniqueSessionCode()
        val allowedUsers = userRepository.findAllById(allowedUserIds).toMutableSet()

        return sessionRepository.save(
            SessionEntity(
                quiz = quiz,
                sessionCode = sessionCode,
                accessType = SessionAccessType.PRIVATE,
                accessibleUsers = allowedUsers,
                startTime = LocalDateTime.now()
            )
        )
    }

    fun addAccessToPrivateSession(sessionId: UUID, userId: UUID): SessionEntity? {
        val session = sessionRepository.findById(sessionId).orElse(null)
        val user = userRepository.findById(userId).orElse(null)

        if (session != null && user != null && session.accessType == SessionAccessType.PRIVATE) {
            session.accessibleUsers.add(user)
            user.sessions.add(session) // Keep bidirectional consistent
            return sessionRepository.save(session)
        }
        return null
    }
}