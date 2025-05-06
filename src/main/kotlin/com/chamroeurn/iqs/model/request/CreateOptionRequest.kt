package com.chamroeurn.iqs.model.request

import com.chamroeurn.iqs.config.constants.AppConstants
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class CreateOptionRequest(
    @field:NotBlank(message = "The field optionText is required.")
    val optionText: String,

    val isCorrect: Boolean = false,

    @field:NotBlank(message = "The field questionId is required.")
    @field:Pattern(regexp = AppConstants.UUID_REGEX, message = "The field questionId must be a valid UUID.")
    val questionId: String
)
