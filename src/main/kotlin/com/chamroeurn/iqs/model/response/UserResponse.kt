package com.chamroeurn.iqs.model.response

import com.chamroeurn.iqs.repository.entity.UserEntity

data class UserResponse(
    val userId: String,
    val email: String,
    val username: String,
    val userRole: String
)

fun UserEntity.toUserResponse() = UserResponse(
    userId = userId.toString(),
    email = email,
    userRole = role.name,
    username = username
)
