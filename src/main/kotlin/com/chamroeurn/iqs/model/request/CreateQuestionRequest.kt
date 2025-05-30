package com.chamroeurn.iqs.model.request

import com.chamroeurn.iqs.config.constants.AppConstants
import jakarta.validation.constraints.*

data class CreateQuestionRequest(
    @field:NotBlank(message = "The field quizId is required.")
    @field:Pattern(regexp = AppConstants.UUID_REGEX, message = "The field quizId must be a valid UUID.")
    val quizId: String,

    @field:NotBlank(message = "The field questionText is required.")
    val questionText: String,

    @field:NotNull(message = "The field timeLimitInSecond is required.")
    @field:Digits(integer = Int.MAX_VALUE.toString().length, fraction = 0, message = "The field timeLimitInSecond must be an integer.")
    @field:Min(value = 11, message = "The field timeLimitInSecond must be greater than 10.")
    val timeLimitInSecond: Int
)
