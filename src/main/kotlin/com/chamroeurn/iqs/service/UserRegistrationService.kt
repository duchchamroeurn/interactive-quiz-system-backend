package com.chamroeurn.iqs.service

import com.chamroeurn.iqs.exception.RestErrorResponseException
import com.chamroeurn.iqs.model.request.RegisterRequest
import com.chamroeurn.iqs.model.request.toEntity
import com.chamroeurn.iqs.model.response.RegisterResponse
import com.chamroeurn.iqs.model.response.SuccessResponse
import com.chamroeurn.iqs.model.response.toRegisterResponse
import com.chamroeurn.iqs.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserRegistrationService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun registerUser(registerRequest: RegisterRequest): SuccessResponse<RegisterResponse> {
        val errors = mutableMapOf<String, String>()

        if (userRepository.existsByEmail(registerRequest.email)) {
            errors["email"] = "Email is already token."
        }
        if (userRepository.existsByUsername(registerRequest.username)) {
            errors["username"] = "Username is already token."
        }

        if (errors.isNotEmpty()) {
            val problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                "Request validation failed"
            )
            problemDetail.properties = errors as Map<String, Any>?

            throw RestErrorResponseException(problemDetail)

        }
        val userEntity = registerRequest.toEntity()
        val user = userEntity.apply { password = passwordEncoder.encode(userEntity.password) }

        val userCreated = userRepository.save(user)

        return SuccessResponse(
            data = userCreated.toRegisterResponse(),
            message = "You're all set! Your registration was successful."
        )
    }
}