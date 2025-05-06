package com.chamroeurn.iqs.model.request

import com.chamroeurn.iqs.config.constants.AppConstants
import jakarta.validation.Valid
import jakarta.validation.constraints.*

data class CreateQuestionWithOptionsRequest(
    @field:NotBlank(message = "The field quizId is required.")
    @field:Pattern(regexp = AppConstants.UUID_REGEX, message = "The field quizId must be a valid UUID.")
    val quizId: String,

    @field:NotBlank(message = "The field questionText is required.")
    val questionText: String,

    @field:NotNull(message = "The field timeLimitInSecond is required.")
    @field:Digits(integer = Int.MAX_VALUE.toString().length, fraction = 0, message = "The field timeLimitInSecond must be an integer.")
    @field:Min(value = 11, message = "The field timeLimitInSecond must be greater than 10.")
    val timeLimitInSecond: Int,

    @field:Valid
    @field:Size(min = 2, max = 4, message = "Question Options must have at least two and at most four options.")
    val options: List<QuestionOptionRequest>
)

data class QuestionOptionRequest(
    @field:NotBlank(message = "The field optionText is required.")
    val optionText: String,

//    @field:JsonProperty("isCorrect")
    @field:NotNull(message = "Option must be either true or false")
    val isCorrect: Boolean
)

//{
//    @AssertTrue(message = "At least one option must be correct")
//    private fun isAtLeastOneCorrect(): Boolean {
//        return options.any { it.isCorrect }
//    }
//}