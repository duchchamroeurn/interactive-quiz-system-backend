package com.chamroeurn.iqs.model.request

import jakarta.validation.constraints.Digits
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class UpdateQuestionRequest(
    @field:NotBlank(message = "The field questionText is required.")
    val questionText: String,

    @field:NotNull(message = "The field timeLimitInSecond is required.")
    @field:Digits(integer = Int.MAX_VALUE.toString().length, fraction = 0, message = "The field timeLimitInSecond must be an integer.")
    @field:Min(value = 11, message = "The field timeLimitInSecond must be greater than 10.")
    val timeLimitInSecond: Int
)
