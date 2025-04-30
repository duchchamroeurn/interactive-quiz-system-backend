package com.chamroeurn.iqs.model.request

import jakarta.validation.constraints.NotBlank

data class UpdateOptionRequest(
    @field:NotBlank(message = "The field optionText is required.")
    val optionText: String,

    val isCorrect: Boolean = false
)
