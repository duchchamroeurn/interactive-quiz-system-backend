package com.chamroeurn.iqs.model.request

import com.chamroeurn.iqs.config.constants.AppConstants
import jakarta.validation.constraints.Pattern

data class StartSessionRequest(
    @field:Pattern(regexp = AppConstants.UUID_REGEX, message = "Quiz ID must be a valid UUID.")
    val quizId: String,

    val users: List<String>
)
