package com.chamroeurn.iqs.service

import com.chamroeurn.iqs.model.request.AuthenticationRequest
import com.chamroeurn.iqs.model.response.SuccessResponse
import com.chamroeurn.iqs.model.response.UserResponse
import com.chamroeurn.iqs.model.response.toUserResponse
import com.chamroeurn.iqs.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val userRepository: UserRepository,
    private val authenticationManager: AuthenticationManager,
) {
    fun authenticate(authenticationRequest: AuthenticationRequest): SuccessResponse<UserResponse> {
        val auth = UsernamePasswordAuthenticationToken.unauthenticated(authenticationRequest.username, authenticationRequest.password)
        authenticationManager.authenticate(auth)

        val user = userRepository.findByEmail(authenticationRequest.username)
            ?: throw UsernameNotFoundException("User with username [${authenticationRequest.username}] not found")
        return SuccessResponse(
            message = "Login success", data = user.toUserResponse());
    }
}