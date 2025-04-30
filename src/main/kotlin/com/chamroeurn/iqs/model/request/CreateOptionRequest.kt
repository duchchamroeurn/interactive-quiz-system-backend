package com.chamroeurn.iqs.model.request

import jakarta.validation.constraints.NotBlank

data class CreateOptionRequest(
    @field:NotBlank(message = "The field optionText is required.")
    val optionText: String,

    val isCorrect: Boolean = false,

    @field:NotBlank(message = "The field questionId is required.")
    val questionId: String
)
