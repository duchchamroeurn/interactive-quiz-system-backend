package com.chamroeurn.iqs.service

import com.chamroeurn.iqs.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class JpaUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val userEntity = userRepository.findByEmail(username)
            ?: throw UsernameNotFoundException("User with username $username not fount")

        return User
            .builder()
            .username(username)
            .password(userEntity.password)
            .roles(userEntity.role.name)
            .build()
    }
}
