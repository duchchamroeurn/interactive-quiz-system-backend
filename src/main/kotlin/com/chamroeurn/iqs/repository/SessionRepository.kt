package com.chamroeurn.iqs.repository

import com.chamroeurn.iqs.repository.entity.SessionEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface SessionRepository: JpaRepository<SessionEntity, UUID> {
    fun existsBySessionCode(sessionCode: String): Boolean
    fun findBySessionCode(sessionCode: String): SessionEntity?

    // Find sessions accessible by a specific user (for PRIVATE)
    @Query("""
        SELECT s FROM SessionEntity s
        JOIN s.accessibleUsers u
        WHERE u.userId = :userId
        AND (s.accessType = 'PRIVATE')
    """)
    fun findAccessibleSessionsByUserId(@Param("userId") userId: UUID, pageable: Pageable): Page<SessionEntity>
}