package com.chamroeurn.iqs.model.response

import com.chamroeurn.iqs.repository.entity.UserEntity

data class RegisterResponse(
    val userId: String,
    val username: String,
    val email: String,
    val userRole: String
)

fun UserEntity.toRegisterResponse() =
    RegisterResponse(userId = userId.toString(), username = username, email = email, userRole = role.name)