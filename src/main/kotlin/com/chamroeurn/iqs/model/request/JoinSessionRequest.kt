package com.chamroeurn.iqs.model.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.UUID

data class JoinSessionRequest(
    @field:NotBlank(message = "The field sessionCode is required.")
    val sessionCode: String,

    @field:NotNull(message = "User ID cannot be null")
    val userId: UUID
)
