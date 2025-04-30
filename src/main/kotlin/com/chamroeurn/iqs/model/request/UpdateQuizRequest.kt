package com.chamroeurn.iqs.model.request

import jakarta.validation.constraints.NotBlank

data class UpdateQuizRequest(
    @field:NotBlank(message = "The field quiz title is required.")
    val title: String,
)
