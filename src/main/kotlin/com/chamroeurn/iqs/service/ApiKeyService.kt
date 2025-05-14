package com.chamroeurn.iqs.service

import com.chamroeurn.iqs.repository.UserRepository
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ApiKeyService(
    private val userRepository: UserRepository
) {

    fun authenticate(apiKey: String?): UsernamePasswordAuthenticationToken? {
        try {
            val userId = UUID.fromString(apiKey)
            val user = userRepository.findById(userId).orElseThrow { throw Exception() }
            return UsernamePasswordAuthenticationToken(user.email, user.password, listOf(SimpleGrantedAuthority(user.role.name)))

        } catch (error: Exception){
            return null
        }

    }
}