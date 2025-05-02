package com.chamroeurn.iqs.model.request

import jakarta.validation.constraints.Pattern


private const val uuidRegexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"

data class SubmitAnswerRequest(
    @field:Pattern(regexp = uuidRegexp, message = "Session ID must be a valid UUID.")
    val sessionId: String,

    @field:Pattern(regexp = uuidRegexp, message = "User ID must be a valid UUID.")
    val userId: String,

    @field:Pattern(regexp = uuidRegexp, message = "Question ID must be a valid UUID.")
    val questionId: String,

    @field:Pattern(regexp = uuidRegexp, message = "Option ID must be a valid UUID.")
    val optionId: String
)
