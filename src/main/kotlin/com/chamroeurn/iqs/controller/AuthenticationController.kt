package com.chamroeurn.iqs.controller

import com.chamroeurn.iqs.model.request.AuthenticationRequest
import com.chamroeurn.iqs.model.request.RegisterRequest
import com.chamroeurn.iqs.model.response.RegisterResponse
import com.chamroeurn.iqs.model.response.SuccessResponse
import com.chamroeurn.iqs.model.response.UserResponse
import com.chamroeurn.iqs.service.AuthenticationService
import com.chamroeurn.iqs.service.UserRegistrationService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/auth")
class AuthenticationController(
    private val userRegistrationService: UserRegistrationService,
    private val authenticationService: AuthenticationService
) {

    @PostMapping("/register")
    fun register(@Valid @RequestBody body: RegisterRequest): ResponseEntity<SuccessResponse<RegisterResponse>> {
        val registerResponse = userRegistrationService.registerUser(body)
        return ResponseEntity.ok(registerResponse)
    }

    @PostMapping("/sign-in")
    fun authenticate(@Valid @RequestBody authenticationRequest: AuthenticationRequest): ResponseEntity<SuccessResponse<UserResponse>> {
        val authResponse =
            authenticationService.authenticate(authenticationRequest)
        return ResponseEntity.ok(authResponse)
    }
}