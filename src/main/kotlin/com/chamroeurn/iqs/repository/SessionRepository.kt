package com.chamroeurn.iqs.repository

import com.chamroeurn.iqs.repository.entity.SessionEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional
import java.util.UUID

interface SessionRepository: JpaRepository<SessionEntity, UUID> {
    fun existsBySessionCode(sessionCode: String): Boolean
    fun findBySessionCode(sessionCode: String): SessionEntity?

    // Find sessions accessible by a specific user (for PRIVATE)
    @Query("""
        SELECT s FROM SessionEntity s
        LEFT JOIN s.accessibleUsers u
        LEFT JOIN s.submittedAnswers a_check ON a_check.session.sessionId = s.sessionId AND a_check.user.userId = :userId
        WHERE u.userId = :userId
        AND (s.accessType = 'PRIVATE')
        AND s.endTime IS NULL
        AND a_check.id IS NULL
    """)
    fun findAccessibleSessionsByUserId(@Param("userId") userId: UUID, pageable: Pageable): Page<SessionEntity>

    @Query("""
        SELECT s FROM SessionEntity s
        LEFT JOIN s.submittedAnswers a_check ON a_check.session.sessionId = s.sessionId AND a_check.user.userId = :userId
        WHERE s.accessType = 'PUBLIC'
        AND s.endTime IS NULL
        AND a_check.id IS NULL
    """)
    fun findPublicSessionsOpenAndNotStartedByUser(
        @Param("userId") userId: UUID,
        pageable: Pageable
    ): Page<SessionEntity>

    @Query("""
        SELECT s FROM SessionEntity s
        LEFT JOIN s.accessibleUsers u
        LEFT JOIN s.submittedAnswers a_check ON a_check.session.sessionId = s.sessionId AND a_check.user.userId = :userId
        WHERE s.sessionCode = :sessionCode
        AND (
            s.accessType = 'PUBLIC'
            OR
            (s.accessType = 'PRIVATE' AND u.userId = :userId)
        )
        AND s.endTime IS NULL
        AND a_check.id IS NULL
    """)
    fun findAvailableSessionNotAnsweredByCodeAndUser(
        @Param("sessionCode") sessionCode: String,
        @Param("userId") userId: UUID
    ): Optional<SessionEntity>
}