package com.chamroeurn.iqs.repository

import com.chamroeurn.iqs.repository.entity.UserEntity
import com.chamroeurn.iqs.repository.entity.UserRoles
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepository: JpaRepository<UserEntity, UUID> {
    fun findByUsername(username: String): UserEntity?
    fun findByEmail(email: String): UserEntity?
    fun existsByUsername(username: String): Boolean
    fun existsByEmail(email: String): Boolean
    // Modified method to include pagination
    fun findByRoleAndUsernameContainingIgnoreCase(role: UserRoles, username: String, pageable: PageRequest): List<UserEntity>
}