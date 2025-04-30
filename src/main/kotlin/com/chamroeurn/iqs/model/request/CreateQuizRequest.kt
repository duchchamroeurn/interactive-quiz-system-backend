package com.chamroeurn.iqs.model.request

import com.chamroeurn.iqs.repository.entity.QuizEntity
import jakarta.validation.constraints.NotBlank

data class CreateQuizRequest(
    @field:NotBlank(message = "The field quiz title is required.")
    val title: String,

    @field:NotBlank(message = "The field presenter Id is required.")
    val presenterId: String
)