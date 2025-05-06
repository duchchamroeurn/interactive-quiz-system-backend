package com.chamroeurn.iqs.model.request


import com.chamroeurn.iqs.config.constants.AppConstants
import jakarta.validation.constraints.Pattern

data class SubmitAnswerRequest(
    @field:Pattern(regexp = AppConstants.UUID_REGEX, message = "Session ID must be a valid UUID.")
    val sessionId: String,

    @field:Pattern(regexp = AppConstants.UUID_REGEX, message = "User ID must be a valid UUID.")
    val userId: String,

    @field:Pattern(regexp = AppConstants.UUID_REGEX, message = "Question ID must be a valid UUID.")
    val questionId: String,

    @field:Pattern(regexp = AppConstants.UUID_REGEX, message = "Option ID must be a valid UUID.")
    val optionId: String
)
