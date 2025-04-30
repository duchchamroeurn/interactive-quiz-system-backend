package com.chamroeurn.iqs.repository

import com.chamroeurn.iqs.repository.entity.SessionEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SessionRepository: JpaRepository<SessionEntity, UUID>