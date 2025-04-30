package com.chamroeurn.iqs.model.request

import com.chamroeurn.iqs.model.response.RegisterResponse
import com.chamroeurn.iqs.model.response.SuccessResponse
import com.chamroeurn.iqs.repository.entity.UserEntity
import com.chamroeurn.iqs.repository.entity.UserRoles
import com.chamroeurn.iqs.validation.ValueOfEnum
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

data class RegisterRequest(

    @field:NotBlank(message = "The username is required.")
    @field:Length(
        min = 6,
        max = 50,
        message = "The username must be at least 6 characters and at most 50 characters long."
    )
    val username: String,

    @field:Email(message = "The email address is invalid.")
    @field:NotBlank(message = "The email address is required.")
    @field:Length(max = 100, message = "The length of the email address must not exceed 100 characters.")
    val email: String,

    @field:NotBlank(message = "The password is required.")
    @field:Length(min = 6, message = "The password must have a minimum length of 6 characters.")
    val password: String,

    @field:ValueOfEnum(enumClass = UserRoles::class, message = "The provided user role is not valid.")
    val role: String?
)


fun RegisterRequest.toEntity() = UserEntity(
    username = username,
    email = email,
    password = password,
    role = role?.let {
        try {
            UserRoles.valueOf(it.uppercase())
        } catch (e: IllegalArgumentException) {
            UserRoles.AUDIENCE
        }
    } ?: UserRoles.AUDIENCE
)