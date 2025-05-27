package com.chamroeurn.iqs.model.response

import com.chamroeurn.iqs.repository.entity.QuizEntity
import com.chamroeurn.iqs.repository.entity.UserEntity
import java.util.UUID

data class DropdownResponse(
    val title: String,
    val value: UUID
)

fun QuizEntity.toDropdownResponse() = quizId?.let { DropdownResponse(title = title, value = it) }

fun UserEntity.toDropdownResponse() = userId?.let { DropdownResponse(title = username, value = it) }